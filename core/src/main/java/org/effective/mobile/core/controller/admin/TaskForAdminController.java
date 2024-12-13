package org.effective.mobile.core.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.effective.mobile.core.dto.TaskRequest;
import org.effective.mobile.core.entity.Task;
import org.effective.mobile.core.entity.enums.Priority;
import org.effective.mobile.core.entity.enums.Status;
import org.effective.mobile.core.exception.TaskNotFoundException;
import org.effective.mobile.core.mapper.TaskMapper;
import org.effective.mobile.core.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/tasks")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Task с доступом для ADMIN")
public class TaskForAdminController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    public TaskForAdminController(TaskService taskService,
                                  TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @Operation(summary = "Создать задачу", description = "Создает новую задачу.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Задача создана", content = @Content(schema = @Schema(implementation = Task.class)))
    })
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody @Valid TaskRequest request) {
        Task taskAfterMapping = new Task();
        taskMapper.updateTaskFromRequest(request, taskAfterMapping);
        Task task = taskService.createTask(taskAfterMapping);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @Operation(summary = "Получить задачу по ID", description = "Возвращает задачу по указанному идентификатору.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Задача найдена", content = @Content(schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long taskId) throws TaskNotFoundException {
        Task task = taskService.getTaskById(taskId);
        return ResponseEntity.ok(task);
    }

    @Operation(summary = "Обновить задачу", description = "Обновляет данные задачи по указанному ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Задача обновлена"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    @PutMapping("/{taskId}")
    public ResponseEntity<Void> updateTask(@PathVariable Long taskId, @RequestBody @Valid TaskRequest request) throws TaskNotFoundException {
        taskService.updateTask(taskId, request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Обновить приоритет задачи", description = "Изменяет приоритет задачи по указанному ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Приоритет обновлён"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    @PutMapping("/{taskId}/priority")
    public ResponseEntity<Void> updateTaskPriority(@PathVariable Long taskId, @RequestBody @Valid Priority newPriority) throws TaskNotFoundException {
        TaskRequest taskRequest = TaskRequest.builder()
                .priority(newPriority)
                .build();
        taskService.updateTask(taskId, taskRequest);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Обновить статус задачи", description = "Изменяет статус задачи по указанному ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Статус обновлён"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    @PutMapping("/{taskId}/status")
    public ResponseEntity<Void> updateTaskStatus(@PathVariable Long taskId, @RequestBody @Valid Status newStatus) throws TaskNotFoundException {
        TaskRequest taskRequest = TaskRequest.builder()
                .status(newStatus)
                .build();
        taskService.updateTask(taskId, taskRequest);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Удалить задачу", description = "Удаляет задачу по указанному ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Задача удалена"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) throws TaskNotFoundException {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }
}
