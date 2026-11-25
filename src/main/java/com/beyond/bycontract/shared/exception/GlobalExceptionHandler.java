package com.beyond.bycontract.shared.exception;


import com.beyond.bycontract.user.domain.exception.UserAlreadyExistsException;
import com.beyond.bycontract.user.domain.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	// ***** USER ********

	@ExceptionHandler(UserNotFoundException.class)
	ResponseEntity<GlobalErrorResponse> handleUserNotFound(UserNotFoundException exc) {
		return new ResponseEntity<>(new GlobalErrorResponse(HttpStatus.NOT_FOUND.value(), exc.getMessage()), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UserAlreadyExistsException.class)
	ResponseEntity<GlobalErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException exc) {
		return new ResponseEntity<>(new GlobalErrorResponse(HttpStatus.CONFLICT.value(), exc.getMessage()), HttpStatus.CONFLICT);
	}

	// ***** GENERIC EXCEPTION ********

//	@ExceptionHandler(Exception.class)
//	ResponseEntity<GlobalErrorResponse> handleGenericException(Exception exc) {
//		return new ResponseEntity<>(new GlobalErrorResponse(HttpStatus.BAD_REQUEST.value(), exc.getMessage()), HttpStatus.BAD_REQUEST);
//	}

	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse handleNotFound(ResourceNotFoundException ex) {
		return new ErrorResponse(
				HttpStatus.NOT_FOUND.value(),
				"Ressource introuvable",
				ex.getMessage(),
				LocalDateTime.now()
		);
	}

	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ErrorResponse handleAccessDeniedException(AccessDeniedException ex) {
		return new ErrorResponse(
				HttpStatus.FORBIDDEN.value(),
				"Accès refusé",
				ex.getMessage(),
				LocalDateTime.now()
		);
	}

	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleRuntimeException(RuntimeException ex) {
		return new ErrorResponse(
				HttpStatus.BAD_REQUEST.value(),
				"Erreur de traitement",
				ex.getMessage(), // Affichera : "Impossible de récupérer l'historique de facturation."
				LocalDateTime.now()
		);
	}


	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorResponse handleGenericException(Exception ex) {
		return new ErrorResponse(
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Erreur technique",
				"Une erreur inattendue est survenue sur le serveur.",
				LocalDateTime.now()
		);
	}

	// Le format du JSON renvoyé à Next.js
	public record ErrorResponse(
			int status,
			String error,
			String message,
			LocalDateTime timestamp
	) {}
}
