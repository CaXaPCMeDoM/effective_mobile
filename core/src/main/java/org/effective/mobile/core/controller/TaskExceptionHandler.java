package org.effective.mobile.core.controller;

import org.effective.mobile.ApiErrorResponse;
import org.effective.mobile.core.controller.admin.TaskForAdminController;
import org.effective.mobile.core.controller.user.TaskForUserController;
import org.effective.mobile.core.exception.TaskNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {
        TaskForAdminController.class,
        TaskForUserController.class}
)
public class TaskExceptionHandler {
    private static final String TASK_NOT_FOUND_MESSAGE = "Task not found";
    private static final String ACCESS_DENIED_MESSAGE = "Access denied. You must be logged in.";

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleTaskNotFound(TaskNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiErrorResponse.builder()
                        .description(TASK_NOT_FOUND_MESSAGE)
                        .build());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiErrorResponse.builder()
                        .description(ACCESS_DENIED_MESSAGE)
                        .build());
    }
}
