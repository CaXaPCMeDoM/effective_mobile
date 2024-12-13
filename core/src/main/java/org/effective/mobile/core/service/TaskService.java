package org.effective.mobile.core.service;

import org.effective.mobile.core.dto.TaskRequest;
import org.effective.mobile.core.entity.Task;
import org.effective.mobile.core.entity.TaskFilterParams;
import org.effective.mobile.core.exception.TaskNotFoundException;
import org.effective.mobile.core.mapper.TaskMapper;
import org.effective.mobile.core.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для управления задачами.
 */
@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    /**
     * Создаёт новую задачу.
     *
     * @param task задача для создания
     * @return созданная задача
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Task createTask(Task task) {
        LocalDateTime time = LocalDateTime.now();
        task.setCreatedAt(time);
        task.setUpdatedAt(time);
        return taskRepository.create(task);
    }

    /**
     * Обновляет существующую задачу.
     * @param id ID задачи для обновления
     * @param taskRequest запрос на обновление задачи
     * @throws TaskNotFoundException если задача не найдена
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void updateTask(Long id, TaskRequest taskRequest) throws TaskNotFoundException {
        LocalDateTime time = LocalDateTime.now();

        Task existingTask = taskRepository.findById(id)
                .orElseThrow(TaskNotFoundException::new);

        taskMapper.updateTaskFromRequest(taskRequest, existingTask);

        existingTask.setUpdatedAt(time);

        taskRepository.update(existingTask);
    }

    /**
     * Возвращает задачу по ID.
     * @param id ID задачи
     * @return найденная задача
     * @throws TaskNotFoundException если задача не найдена
     */
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public Task getTaskById(Long id) throws TaskNotFoundException {
        Optional<Task> taskOptional = taskRepository.findById(id);
        return taskOptional.orElseThrow(
                () -> new TaskNotFoundException("Task with ID " + id + " not found")
        );
    }

    /**
     * Возвращает все задачи.
     * @return список всех задач
     */
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<Task> getAllTasks() {
        return taskRepository.getAll();
    }

    /**
     * Возвращает задачи с применением фильтров.
     * @param filterParams параметры фильтрации задач
     * @return список задач, соответствующих фильтрам
     */
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<Task> getTasks(TaskFilterParams filterParams) {
        return taskRepository.getTaskWithFilterByTaskFilterParams(filterParams);
    }

    /**
     * Удаляет задачу по ID.
     * @param id ID задачи
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void deleteTask(Long id) throws TaskNotFoundException {
        taskRepository.deleteById(id);
    }
}
