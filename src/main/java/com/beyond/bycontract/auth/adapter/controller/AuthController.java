package com.beyond.bycontract.auth.adapter.controller;

import com.beyond.bycontract.auth.application.dto.AuthResponseDto;
import com.beyond.bycontract.auth.application.dto.LoginRequestDto;
import com.beyond.bycontract.auth.application.dto.RegisterRequestDto;
import com.beyond.bycontract.auth.application.service.AuthService;
import com.beyond.bycontract.user.infrastructure.entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	private static final long COOKIE_MAX_AGE_SECONDS = 24 * 60 * 60;

	private final AuthService authService;
	private final boolean cookieSecure;
	private final String cookieSameSite;

	public AuthController(
			AuthService authService,
			@Value("${application.cookie.secure}") boolean cookieSecure,
			@Value("${application.cookie.same-site}") String cookieSameSite
	) {
		this.authService = authService;
		this.cookieSecure = cookieSecure;
		this.cookieSameSite = cookieSameSite;
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterRequestDto registerRequestDto) {
		AuthResponseDto response = authService.register(registerRequestDto);
		return buildCookieResponse(response.getToken(), "Register successfully");
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) {
		AuthResponseDto response = authService.login(loginRequestDto);
		return buildCookieResponse(response.getToken(), "Login successfully");
	}

	private ResponseEntity<?> buildCookieResponse(String token, String message) {
		ResponseCookie jwtCookie = ResponseCookie.from("jwt", token)
				.httpOnly(true) // Invisible for the javascript to prevent the XSS Attack
				.secure(cookieSecure) //To put a true in production for the HTTPS
				.path("/") //Send the cookies on all the routes
				.maxAge(24 * 60 * 60) //Life 1 day
				.sameSite(cookieSameSite) //Protection CSRF
				.build();

		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
				.body(Map.of("message", message));
	}

	@GetMapping("/me")
	public ResponseEntity<?> getCurrentUser() {
		// Ask at the spring boot wall who is authenticated
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		//If no one authenticated or no cookie, send an error
		if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
			return ResponseEntity.status(401).body(Map.of("error", "Non authenticated"));
		}

		UserEntity currentUser = (UserEntity) authentication.getPrincipal();

		return ResponseEntity.ok(Map.of(
				"id", currentUser.getId(),
				"email", currentUser.getEmail(),
				"firstName", currentUser.getFirstName(),
				"lastName", currentUser.getLastName(),
				"role", currentUser.getUserRole()
		));
	}


	@PostMapping("/logout")
	public ResponseEntity<?> logout() {
		ResponseCookie deletedCookie = ResponseCookie.from("jwt", "")
				.httpOnly(true)
				.secure(cookieSecure)
				.path("/")
				.maxAge(0)
				.sameSite(cookieSameSite)
				.build();

		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, deletedCookie.toString())
				.body(Map.of("message", "Logout successfully"));
	}
}
