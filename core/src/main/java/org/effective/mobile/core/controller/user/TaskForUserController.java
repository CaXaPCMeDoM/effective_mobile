package org.effective.mobile.core.controller.user;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.effective.mobile.core.dto.CommentRequest;
import org.effective.mobile.core.dto.TaskRequest;
import org.effective.mobile.core.entity.Comment;
import org.effective.mobile.core.entity.Task;
import org.effective.mobile.core.entity.enums.Status;
import org.effective.mobile.core.exception.TaskNotFoundException;
import org.effective.mobile.core.security.AccessValidator;
import org.effective.mobile.core.service.CommentService;
import org.effective.mobile.core.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/tasks")
@PreAuthorize("hasRole('USER')")
@Tag(name = "Task с доступом для USER")
@RequiredArgsConstructor
public class TaskForUserController {
    private final TaskService taskService;
    private final CommentService commentService;
    private final AccessValidator accessValidator;

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long taskId) throws TaskNotFoundException {
        if (!accessValidator.isTaskAccess(taskId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Task task = taskService.getTaskById(taskId);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{taskId}/status")
    public ResponseEntity<Void> updateTaskStatus(
            @PathVariable Long taskId,
            @RequestBody @Valid Status request
    ) throws TaskNotFoundException {
        if (!accessValidator.isTaskAccess(taskId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        TaskRequest taskRequest = TaskRequest.builder()
                .status(request)
                .build();
        taskService.updateTask(taskId, taskRequest);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{taskId}/comments")
    public ResponseEntity<Comment> addCommentToTask(
            @PathVariable Long taskId,
            @RequestBody @Valid CommentRequest request
    ) throws TaskNotFoundException {
        if (!accessValidator.isTaskAccess(taskId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        request.setTaskId(taskId);
        Comment comment = commentService.createComment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @GetMapping("/{taskId}/comments")
    public ResponseEntity<List<Comment>> getCommentsByTaskId(@PathVariable Long taskId) throws TaskNotFoundException {
        if (!accessValidator.isTaskAccess(taskId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        List<Comment> comments = commentService.getCommentsByTaskId(taskId);
        return ResponseEntity.ok(comments);
    }
}
