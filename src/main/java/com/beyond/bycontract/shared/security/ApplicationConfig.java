package com.beyond.bycontract.shared.security;

import com.beyond.bycontract.shared.utils.CustomUserDetails;
import com.beyond.bycontract.user.infrastructure.entity.UserEntity;
import com.beyond.bycontract.user.infrastructure.repository.SpringDataUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class ApplicationConfig {

	private final SpringDataUserRepository userRepository;

	public ApplicationConfig(SpringDataUserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return username -> {
			// 1. Récupération de ton entité
			UserEntity userEntity = userRepository.findByEmail(username)
					.orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));

			// 2. Conversion du rôle en autorité Spring Security (sans le "ROLE_")
			List<SimpleGrantedAuthority> authorities = List.of(
					new SimpleGrantedAuthority(userEntity.getUserRole().name())
			);

			// 3. Retourne l'objet que Spring Security attend
			return new CustomUserDetails(
					userEntity.getId(),
					userEntity.getEmail(),
					userEntity.getPassword(),
					userEntity.getFirstName(),
					userEntity.getLastName(),
					authorities
			);
		};
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
