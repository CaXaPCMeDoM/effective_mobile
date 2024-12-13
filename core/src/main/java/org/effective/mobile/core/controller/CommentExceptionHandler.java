package org.effective.mobile.core.controller;

import org.effective.mobile.ApiErrorResponse;
import org.effective.mobile.core.controller.admin.CommentForAdminController;
import org.effective.mobile.core.exception.CommentNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(
        assignableTypes = CommentForAdminController.class
)
public class CommentExceptionHandler {
    private static final String COMMENT_NOT_FOUND_MESSAGE = "Comment not found";

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleCommentNotFoundException(CommentNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiErrorResponse.builder()
                        .description(COMMENT_NOT_FOUND_MESSAGE)
                        .build());
    }
}
