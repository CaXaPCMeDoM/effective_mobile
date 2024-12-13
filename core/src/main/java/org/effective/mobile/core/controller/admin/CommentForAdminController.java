package org.effective.mobile.core.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.effective.mobile.core.dto.CommentRequest;
import org.effective.mobile.core.entity.Comment;
import org.effective.mobile.core.exception.CommentNotFoundException;
import org.effective.mobile.core.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/comments")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@Tag(name = "Comments с доступом для ADMIN")
public class CommentForAdminController {
    private final CommentService commentService;

    @Operation(summary = "Создать комментарий", description = "Создает новый комментарий для задачи.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Комментарий создан", content = @Content(schema = @Schema(implementation = Comment.class)))
    })
    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody @Valid CommentRequest request) {
        Comment comment = commentService.createComment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @Operation(summary = "Получить все комментарии", description = "Возвращает список всех комментариев.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Комментарии найдены",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(
                                            implementation = Comment.class))))
    })
    @GetMapping
    public ResponseEntity<List<Comment>> getAllComments() {
        List<Comment> comments = commentService.getAllComments();
        return ResponseEntity.ok(comments);
    }

    @Operation(summary = "Получить комментарии к задаче", description = "Возвращает список комментариев для задачи по её ID.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Комментарии найдены",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(
                                            implementation = Comment.class))))
    })
    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<Comment>> getCommentsByTaskId(@PathVariable Long taskId) {
        List<Comment> comments = commentService.getCommentsByTaskId(taskId);
        return ResponseEntity.ok(comments);
    }

    @Operation(summary = "Удалить комментарий", description = "Удаляет комментарий по указанному ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Комментарий удалён"),
            @ApiResponse(responseCode = "404", description = "Комментарий не найден")
    })
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) throws CommentNotFoundException {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}

