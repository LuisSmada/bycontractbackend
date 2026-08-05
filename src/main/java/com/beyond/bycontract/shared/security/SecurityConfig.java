package com.beyond.bycontract.shared.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final AuthenticationProvider authenticationProvider;
	private final JwtAuthentificationFilter jwtAuthentificationFilter;
	private final String allowedOrigin;

	public SecurityConfig(
			AuthenticationProvider authenticationProvider,
			JwtAuthentificationFilter jwtAuthentificationFilter,
			@Value("${application.cors.allowed-origins}") String allowedOrigin
	) {
		this.authenticationProvider = authenticationProvider;
		this.jwtAuthentificationFilter = jwtAuthentificationFilter;
		this.allowedOrigin = allowedOrigin;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(
			HttpSecurity http,
			CorsConfigurationSource corsConfigurationSource
	) throws Exception {

		http
				.cors(cors ->
						cors.configurationSource(corsConfigurationSource)
				)
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
						.requestMatchers("/api/v1/auth/**").permitAll()
						.requestMatchers(
								"/swagger-ui/**",
								"/v3/api-docs/**"
						).permitAll()
						.anyRequest().authenticated()
				)
				.sessionManagement(session ->
						session.sessionCreationPolicy(
								SessionCreationPolicy.STATELESS
						)
				)
				.authenticationProvider(authenticationProvider)
				.addFilterBefore(
						jwtAuthentificationFilter,
						UsernamePasswordAuthenticationFilter.class
				);

		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		configuration.setAllowedOrigins(
				List.of(allowedOrigin)
		);

		configuration.setAllowedMethods(
				List.of(
						"GET",
						"POST",
						"PUT",
						"PATCH",
						"DELETE",
						"OPTIONS"
				)
		);

		configuration.setAllowedHeaders(
				List.of(
						"Authorization",
						"Content-Type",
						"Accept",
						"Origin",
						"X-Requested-With",
						"X-XSRF-TOKEN"
				)
		);

		configuration.setAllowCredentials(true);

		configuration.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source =
				new UrlBasedCorsConfigurationSource();

		source.registerCorsConfiguration(
				"/**",
				configuration
		);

		return source;
	}
}