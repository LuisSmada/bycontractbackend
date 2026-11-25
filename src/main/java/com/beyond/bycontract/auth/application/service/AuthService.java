package com.beyond.bycontract.auth.application.service;

import com.beyond.bycontract.auth.application.dto.AuthResponseDto;
import com.beyond.bycontract.auth.application.dto.LoginRequestDto;
import com.beyond.bycontract.auth.application.dto.RegisterRequestDto;
import com.beyond.bycontract.auth.domain.model.PasswordResetToken;
import com.beyond.bycontract.auth.domain.repository.PasswordResetTokenRepository;
import com.beyond.bycontract.auth.infrastructure.mapper.PasswordResetTokenPersistenceMapper;
import com.beyond.bycontract.auth.infrastructure.repository.JpaPasswordResetTokenRepository;
import com.beyond.bycontract.email.domain.repository.EmailRepository;
import com.beyond.bycontract.shared.security.JwtService;
import com.beyond.bycontract.shared.utils.CustomUserDetails;
import com.beyond.bycontract.user.domain.model.User;
import com.beyond.bycontract.user.domain.model.UserRole;
import com.beyond.bycontract.user.domain.repository.UserRepository;
import com.beyond.bycontract.user.infrastructure.entity.UserEntity;
import com.beyond.bycontract.user.infrastructure.repository.JpaUserRepository;
import com.beyond.bycontract.user.infrastructure.repository.SpringDataUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

	@Value("${application.cors.allowed-origins}")
	private String frontendUrl;

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	private final PasswordResetTokenRepository tokenRepository;
	private final EmailRepository emailRepository;


	public AuthResponseDto register(RegisterRequestDto registerRequestDto) {
		if(userRepository.getUserByEmail(registerRequestDto.getEmail()).isPresent()) {
			throw new RuntimeException("This email already exists");
		}

		User user = User.create(registerRequestDto.getFirstName(),registerRequestDto.getLastName(), registerRequestDto.getEmail(), passwordEncoder.encode(registerRequestDto.getPassword()));

		User savedUser = userRepository.createUser(user);

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


	public AuthResponseDto login(LoginRequestDto loginRequestDto) {
		// 1. L'AuthenticationManager de Spring Security fait le sale boulot.
		// Il va hacher le mot de passe reçu et le comparer avec celui de la BDD.
		// Si ça ne correspond pas, il jette une exception (403 Forbidden).
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginRequestDto.getEmail(),
						loginRequestDto.getPassword()
				)
		);

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

		String jwtToken = jwtService.generateToken(userDetails);
		return new AuthResponseDto(jwtToken);
	}

	@Transactional
	public void processForgotPassword(String email) {
		Optional<User> userOpt = userRepository.getUserByEmail(email);
		// Sécurité : Ne jamais renvoyer d'erreur si l'email n'existe pas.
		// Sinon un pirate pourrait tester tous les emails pour voir qui est inscrit.
		if (userOpt.isEmpty()) {
			return; // On fait semblant que tout s'est bien passé
		}

		User user = userOpt.get();
		tokenRepository.deleteByIdUser(user.getId());
		String tokenString = UUID.randomUUID().toString();
		LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(15);
		PasswordResetToken resetToken = new PasswordResetToken(null, tokenString, user.getId(), expiryDate);
		tokenRepository.save(resetToken);
		String resetUrl = frontendUrl + "/reset-password?token=" + tokenString;
		emailRepository.sendPasswordResetEmail(user.getEmail(), resetUrl);
	}

	public void processResetPassword(String token, String newPassword) {
		PasswordResetToken passwordToken = tokenRepository.findByToken(token).orElseThrow(() -> new IllegalArgumentException("Token invalide ou inexistant."));
		if(passwordToken.isExpired()) {
			tokenRepository.deleteByToken(token);
			throw new IllegalArgumentException("Ce lien a expiré. Veuillez refaire une demande.");
		}

		User user = userRepository.getUserById(passwordToken.getIdUser()).orElseThrow(() -> new UsernameNotFoundException("User not found with id " + passwordToken.getIdUser()));
		user.changePassword(passwordEncoder.encode(newPassword));
		userRepository.changeUserPassword(user);

		// 5. 🚨 DÉTRUIRE le token pour qu'il ne soit utilisé qu'une seule fois
		tokenRepository.deleteByToken(token);
	}
}
