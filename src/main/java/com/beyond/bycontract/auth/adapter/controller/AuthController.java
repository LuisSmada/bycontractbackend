package com.beyond.bycontract.auth.adapter.controller;

import com.beyond.bycontract.auth.adapter.dto.*;
import com.beyond.bycontract.auth.application.dto.AuthResponseDto;
import com.beyond.bycontract.auth.application.dto.LoginRequestDto;
import com.beyond.bycontract.auth.application.dto.RegisterRequestDto;
import com.beyond.bycontract.auth.application.service.AuthService;
import com.beyond.bycontract.auth.application.service.RegistrationOtpService;
import com.beyond.bycontract.shared.utils.CustomUserDetails;
import com.beyond.bycontract.user.infrastructure.entity.UserEntity;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
	private final RegistrationOtpService registrationOtpService;
	private final boolean cookieSecure;
	private final String cookieSameSite;

	public AuthController(
			AuthService authService,
			RegistrationOtpService registrationOtpService,
			@Value("${application.cookie.secure}") boolean cookieSecure,
			@Value("${application.cookie.same-site}") String cookieSameSite
	) {
		this.authService = authService;
		this.cookieSecure = cookieSecure;
		this.cookieSameSite = cookieSameSite;
		this.registrationOtpService = registrationOtpService;
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
	public ResponseEntity<GetCurrentUserResponse> getCurrentUser() {
		// Ask at the spring boot wall who is authenticated
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();

		return ResponseEntity.ok(new GetCurrentUserResponse(
				currentUser.getId(),
				currentUser.getFirstName(),
				currentUser.getLastName(),
				currentUser.getUsername(),
				currentUser.getAuthorities().iterator().next().getAuthority()
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

	@PostMapping("/forgot-password")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
		authService.processForgotPassword(request.email());
	}

	@PostMapping("/reset-password")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
		authService.processResetPassword(request.token(), request.newPassword());
	}

	@PostMapping("/register/initiate")
	public ResponseEntity<Void> initiateRegistration(@Valid @RequestBody RegisterInitiateRequest request) {
		registrationOtpService.initiateRegistration(request);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/register/verify")
	public ResponseEntity<?> verifyRegistration(@Valid @RequestBody RegisterVerifyRequest request) {
		AuthResponseDto response = registrationOtpService.verifyRegistration(request);
		return buildCookieResponse(response.getToken(), "Register successfully");
	}

	@PostMapping("/register/resend")
	public ResponseEntity<Void> resendOtp(@Valid @RequestBody ResendOtpRequest request) {
		registrationOtpService.resendOtp(request);
		return ResponseEntity.ok().build();
	}
}
