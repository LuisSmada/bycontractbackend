package com.beyond.bycontract.shared.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final AuthenticationProvider authenticationProvider;
	@Value("${application.cors.allowed-origins}")
	private String allowedOrigins;
	private JwtAuthentificationFilter jwtAuthentificationFilter;

	public SecurityConfig(AuthenticationProvider authenticationProvider, JwtAuthentificationFilter jwtAuthentificationFilter) {
		this.authenticationProvider = authenticationProvider;
		this.jwtAuthentificationFilter = jwtAuthentificationFilter;
	}

	//Configuration of the routes and the filter
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {

		http
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/api/v1/auth/**").permitAll() //Public routes (login, register)
						.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
						.anyRequest().authenticated() //any other requests need a token
				)
				.sessionManagement(session -> session
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS) //API save anything in memory
				)
				.authenticationProvider(authenticationProvider)
				//Place the jwtFilter before Spring filter
				.addFilterBefore(jwtAuthentificationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowedOrigins(List.of(allowedOrigins));

		// "OPTIONS" est crucial : le navigateur fait toujours une requête vide OPTIONS avant une vraie requête pour vérifier les droits
		corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		// Autoriser Next.js à nous envoyer le JWT via l'en-tête "Authorization" et du JSON via "Content-Type"
		corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

		//Authorize the cookie transport because by defautl navigators don't accept to attach cookie on requests from two differents ports
		corsConfiguration.setAllowCredentials(true);

		//Apply all the rules at all the routes of the API
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfiguration);
		return source;
	}

}
