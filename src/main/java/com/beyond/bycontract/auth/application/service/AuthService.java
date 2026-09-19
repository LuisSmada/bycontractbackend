package com.beyond.bycontract.auth.application.service;

import com.beyond.bycontract.auth.application.dto.AuthResponseDto;
import com.beyond.bycontract.auth.application.dto.LoginRequestDto;
import com.beyond.bycontract.auth.application.dto.RegisterRequestDto;
import com.beyond.bycontract.shared.security.JwtService;
import com.beyond.bycontract.shared.utils.CustomUserDetails;
import com.beyond.bycontract.user.domain.model.UserRole;
import com.beyond.bycontract.user.infrastructure.entity.UserEntity;
import com.beyond.bycontract.user.infrastructure.repository.SpringDataUserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

	private final SpringDataUserRepository springDataUserRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

	public AuthService(SpringDataUserRepository springDataUserRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
		this.springDataUserRepository = springDataUserRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
	}

	public AuthResponseDto register(RegisterRequestDto registerRequestDto) {
		if (springDataUserRepository.findByEmail(registerRequestDto.getEmail()).isPresent()) {
			throw new RuntimeException("This email already exists");
		}

		UserEntity userEntity = new UserEntity();
		userEntity.setFirstName(registerRequestDto.getFirstName());
		userEntity.setLastName(registerRequestDto.getLastName());
		userEntity.setEmail(registerRequestDto.getEmail());
		userEntity.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
		userEntity.setUserRole(UserRole.USER);

		UserEntity savedUser = springDataUserRepository.save(userEntity);

		List<SimpleGrantedAuthority> authorities = List.of(
				new SimpleGrantedAuthority(savedUser.getUserRole().name())
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
}
