package org.effective.mobile.core.repository;

import org.effective.mobile.core.entity.Task;
import org.effective.mobile.core.entity.TaskFilterParams;
import org.effective.mobile.core.exception.TaskNotFoundException;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    Task create(Task task);

    void update(Task task);

    List<Task> getAll();

    int deleteById(Long id) throws TaskNotFoundException;

    Optional<Task> findById(Long id);

    List<Task> getTaskWithFilterByTaskFilterParams(TaskFilterParams taskFilterParams);
}
