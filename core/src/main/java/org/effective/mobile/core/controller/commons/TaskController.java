package org.effective.mobile.core.controller.commons;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.effective.mobile.core.entity.Task;
import org.effective.mobile.core.entity.TaskFilterParams;
import org.effective.mobile.core.entity.enums.Priority;
import org.effective.mobile.core.entity.enums.Status;
import org.effective.mobile.core.service.TaskService;
import org.effective.mobile.core.service.page.PageBuilder;
import org.effective.mobile.core.service.page.TaskPageBuilder;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Task с общим доступом", description = "Доступ есть только у авторизированных пользователей, но с любой ролью")
public class TaskController {
    private final TaskService taskService;
    private final PageBuilder<Task> pageBuilder;

    public TaskController(TaskService taskService,
                          TaskPageBuilder pageBuilder) {
        this.taskService = taskService;
        this.pageBuilder = pageBuilder;
    }

    @Operation(
            summary = "Получить список задач",
            description = """
                    Возвращает список задач с возможностью фильтрации по автору,
                    исполнителю, статусу и приоритету.
                    Также добавлена пагинация.
                    """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Задачи найдены", content = @Content(schema = @Schema(implementation = Page.class)))
    })
    @GetMapping
    public ResponseEntity<Page<Task>> getTasks(
            @RequestParam Optional<Long> authorId,
            @RequestParam Optional<Long> assigneeId,
            @RequestParam Optional<Status> status,
            @RequestParam Optional<Priority> priority,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        TaskFilterParams filterParams = new TaskFilterParams();
        filterParams.setAuthorId(authorId);
        filterParams.setAssigneeId(assigneeId);
        filterParams.setStatus(status);
        filterParams.setPriority(priority);

        List<Task> tasks = taskService.getTasks(filterParams);
        Page<Task> taskPage = pageBuilder.buildPage(tasks, page, size);
        return ResponseEntity.ok(taskPage);
    }
}
