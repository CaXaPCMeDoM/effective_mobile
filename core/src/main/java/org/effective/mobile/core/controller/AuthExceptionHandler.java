package org.effective.mobile.core.controller;

import jakarta.security.auth.message.AuthException;
import org.effective.mobile.ApiErrorResponse;
import org.effective.mobile.core.controller.auth.AuthController;
import org.effective.mobile.core.exception.BadAuthorizationDataException;
import org.effective.mobile.core.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = AuthController.class)
public class AuthExceptionHandler {
    private static final String UNAUTHORIZED_MESSAGE = "Unauthorized";
    private static final String USER_NOT_FOUND_MESSAGE = "User not found";
    private static final String BAD_AUTHORIZATION_DATA_MESSAGE = "An error in the email or password";

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthException(AuthException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiErrorResponse.builder()
                        .description(UNAUTHORIZED_MESSAGE)
                        .build());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiErrorResponse.builder()
                        .description(USER_NOT_FOUND_MESSAGE)
                        .build());
    }
    @ExceptionHandler(BadAuthorizationDataException.class)
    public ResponseEntity<ApiErrorResponse> handlerBadAuthDataException(BadAuthorizationDataException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiErrorResponse.builder()
                        .description(BAD_AUTHORIZATION_DATA_MESSAGE)
                        .build());
    }
}
