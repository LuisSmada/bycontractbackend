package com.beyond.bycontract.shared.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthentificationFilter
		extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;

	public JwtAuthentificationFilter(
			JwtService jwtService,
			UserDetailsService userDetailsService
	) {
		this.jwtService = jwtService;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected boolean shouldNotFilter(
			@NonNull HttpServletRequest request
	) {
		return HttpMethod.OPTIONS.matches(
				request.getMethod()
		);
	}

	@Override
	protected void doFilterInternal(
			@NonNull HttpServletRequest request,
			@NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain
	) throws ServletException, IOException {

		String jwt = extractJwtFromCookies(request);

		if (jwt == null) {
			filterChain.doFilter(request, response);
			return;
		}

		String userEmail;

		try {
			userEmail = jwtService.extractUsername(jwt);
		} catch (Exception exception) {
			/*
			 * Le cookie est invalide ou expiré.
			 * On laisse Spring Security refuser ensuite
			 * l'accès si la route est protégée.
			 */
			filterChain.doFilter(request, response);
			return;
		}

		if (
				userEmail != null
						&& SecurityContextHolder
						.getContext()
						.getAuthentication() == null
		) {
			UserDetails userDetails =
					userDetailsService.loadUserByUsername(
							userEmail
					);

			if (jwtService.isTokenValid(jwt, userDetails)) {
				UsernamePasswordAuthenticationToken authentication =
						new UsernamePasswordAuthenticationToken(
								userDetails,
								null,
								userDetails.getAuthorities()
						);

				authentication.setDetails(
						new WebAuthenticationDetailsSource()
								.buildDetails(request)
				);

				SecurityContextHolder
						.getContext()
						.setAuthentication(authentication);
			}
		}

		filterChain.doFilter(request, response);
	}

	private String extractJwtFromCookies(
			HttpServletRequest request
	) {
		Cookie[] cookies = request.getCookies();

		if (cookies == null) {
			return null;
		}

		for (Cookie cookie : cookies) {
			if ("jwt".equals(cookie.getName())) {
				return cookie.getValue();
			}
		}

		return null;
	}
}