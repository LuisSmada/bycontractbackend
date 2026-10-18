package com.beyond.bycontract.shared.exception;


import com.beyond.bycontract.user.domain.exception.UserAlreadyExistsException;
import com.beyond.bycontract.user.domain.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
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

	@ExceptionHandler(Exception.class)
	ResponseEntity<GlobalErrorResponse> handleGenericException(Exception exc) {
		return new ResponseEntity<>(new GlobalErrorResponse(HttpStatus.BAD_REQUEST.value(), exc.getMessage()), HttpStatus.BAD_REQUEST);
	}
}
