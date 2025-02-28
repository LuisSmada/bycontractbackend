package com.beyond.bycontract.adapter.infrastructure.exception;

import com.beyond.bycontract.adapter.infrastructure.exception.file.FileAlreadyExistsException;
import com.beyond.bycontract.adapter.infrastructure.exception.file.FileNotFoundException;
import com.beyond.bycontract.adapter.infrastructure.exception.folder.FolderAlreadyExistsException;
import com.beyond.bycontract.adapter.infrastructure.exception.folder.FolderNotFoundException;
import com.beyond.bycontract.adapter.infrastructure.exception.user.UserAlreadyExistsException;
import com.beyond.bycontract.adapter.infrastructure.exception.user.UserNotFoundException;
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

    // ***** FOLDER ********

    @ExceptionHandler(FolderNotFoundException.class)
    ResponseEntity<GlobalErrorResponse> handleFolderNotFound(FolderNotFoundException exc) {
        return new ResponseEntity<>(new GlobalErrorResponse(HttpStatus.NOT_FOUND.value(), exc.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FolderAlreadyExistsException.class)
    ResponseEntity<GlobalErrorResponse> handleFolderAlreadyExists(FolderAlreadyExistsException exc) {
        return new ResponseEntity<>(new GlobalErrorResponse(HttpStatus.CONFLICT.value(), exc.getMessage()), HttpStatus.CONFLICT);
    }


    // ***** FILE ********

    @ExceptionHandler(FileNotFoundException.class)
    ResponseEntity<GlobalErrorResponse> handleFileNotFound(FileNotFoundException exc) {
        return new ResponseEntity<>(new GlobalErrorResponse(HttpStatus.NOT_FOUND.value(), exc.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FileAlreadyExistsException.class)
    ResponseEntity<GlobalErrorResponse> handleFileAlreadyExists(FileAlreadyExistsException exc) {
        return new ResponseEntity<>(new GlobalErrorResponse(HttpStatus.CONFLICT.value(), exc.getMessage()), HttpStatus.CONFLICT);
    }

    // ***** GENERIC EXCEPTION ********

    @ExceptionHandler(Exception.class)
    ResponseEntity<GlobalErrorResponse> handleGenericException(Exception exc) {
        return new ResponseEntity<>(new GlobalErrorResponse(HttpStatus.BAD_REQUEST.value(), exc.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
