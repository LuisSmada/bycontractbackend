package com.beyond.bycontract.auth.application.service;

import com.beyond.bycontract.auth.adapter.dto.OtpRequestedEvent;
import com.beyond.bycontract.auth.adapter.dto.RegisterInitiateRequest;
import com.beyond.bycontract.auth.adapter.dto.RegisterVerifyRequest;
import com.beyond.bycontract.auth.adapter.dto.ResendOtpRequest;
import com.beyond.bycontract.auth.application.dto.AuthResponseDto;
import com.beyond.bycontract.auth.domain.model.PendingRegistration;
import com.beyond.bycontract.auth.domain.repository.PendingRegistrationRepository;
import com.beyond.bycontract.auth.infrastructure.entity.PendingRegistrationEntity;
import com.beyond.bycontract.email.domain.repository.EmailRepository;
import com.beyond.bycontract.shared.security.JwtService;
import com.beyond.bycontract.shared.utils.CustomUserDetails;
import com.beyond.bycontract.user.domain.exception.UserNotFoundException;
import com.beyond.bycontract.user.domain.model.User;
import com.beyond.bycontract.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistrationOtpService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final PendingRegistrationRepository pendingRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;
    private final JwtService jwtService;

    // Nettoyage automatique toutes les nuits à 3h00
    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void cleanupExpiredRegistrations() {
        pendingRepo.deleteAllByExpiresAtBefore(LocalDateTime.now());
    }

    private String generateOtp() {
        int code = 100000 + SECURE_RANDOM.nextInt(900000);
        return String.valueOf(code);
    }

    @Transactional
    public void initiateRegistration(RegisterInitiateRequest request) {
        String normalizedEmail = request.email().trim().toLowerCase();

        if (userRepo.getUserByEmail(normalizedEmail).isPresent()) {
            throw new IllegalStateException("Un compte existe déjà avec cette adresse email.");
        }

        String rawOtp = generateOtp();
        String passwordHash = passwordEncoder.encode(request.password());
        String otpHash = passwordEncoder.encode(rawOtp);
        LocalDateTime now = LocalDateTime.now();

        PendingRegistration pending = pendingRepo.getByEmail(normalizedEmail)
                .map(existing -> {
                    existing.restart(
                            request.firstName().trim(),
                            request.lastName().trim(),
                            passwordHash,
                            otpHash,
                            now
                    );
                    return existing;
                })
                .orElseGet(() -> PendingRegistration.create(
                        normalizedEmail,
                        request.firstName().trim(),
                        request.lastName().trim(),
                        passwordHash,
                        otpHash,
                        now
                ));

        pendingRepo.save(pending);
        eventPublisher.publishEvent(new OtpRequestedEvent(normalizedEmail, pending.getFirstName(), rawOtp));
    }

    @Transactional(noRollbackFor = IllegalArgumentException.class)
    public AuthResponseDto verifyRegistration(RegisterVerifyRequest request) {
        String normalizedEmail = request.email().trim().toLowerCase();
        LocalDateTime now = LocalDateTime.now();

        PendingRegistration pending = pendingRepo.getByEmail(normalizedEmail)
                .orElseThrow(() -> new IllegalArgumentException("Aucune inscription en attente pour cet email."));

        if (pending.isExpired(now)) {
            pendingRepo.deleteByEmail(pending.getEmail());
            throw new IllegalArgumentException("Le code a expiré. Veuillez recommencer l'inscription.");
        }

        if (pending.hasReachedMaxAttempts()) {
            pendingRepo.deleteByEmail(pending.getEmail());
            throw new IllegalArgumentException("Trop de tentatives échouées. Veuillez recommencer.");
        }

        if (!passwordEncoder.matches(request.otp(), pending.getOtpHash())) {
            pending.incrementAttempts();
            pendingRepo.save(pending);
            throw new IllegalArgumentException("Code OTP invalide.");
        }

        if (userRepo.getUserByEmail(normalizedEmail).isPresent()) {
            pendingRepo.deleteByEmail(pending.getEmail());
            throw new IllegalStateException("Ce compte existe déjà.");
        }

        // Création définitive du User (passwordHash déjà encodé)
        User user = User.create(
                pending.getFirstName(),
                pending.getLastName(),
                pending.getEmail(),
                pending.getPasswordHash()
        );
        User savedUser = userRepo.createUser(user);

        pendingRepo.deleteByEmail(pending.getEmail());

        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(savedUser.getRole().name())
        );

        CustomUserDetails userDetails = new CustomUserDetails(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getPassword(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                authorities
        );

        String jwtToken = jwtService.generateToken(userDetails);
        return new AuthResponseDto(jwtToken);
    }

    @Transactional
    public void resendOtp(ResendOtpRequest request) {
        String normalizedEmail = request.email().trim().toLowerCase();

        PendingRegistration pending = pendingRepo.getByEmail(normalizedEmail)
                .orElseThrow(() -> new IllegalArgumentException("Aucune inscription en attente."));

        String rawOtp = generateOtp();
        pending.regenerateOtp(passwordEncoder.encode(rawOtp), LocalDateTime.now());

        pendingRepo.save(pending);
        eventPublisher.publishEvent(new OtpRequestedEvent(normalizedEmail, pending.getFirstName(), rawOtp));
    }
}
