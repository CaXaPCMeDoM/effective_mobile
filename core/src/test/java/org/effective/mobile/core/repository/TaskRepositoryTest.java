package org.effective.mobile.core.repository;

import org.effective.mobile.core.entity.Task;
import org.effective.mobile.core.entity.enums.Priority;
import org.effective.mobile.core.entity.enums.Status;
import org.effective.mobile.core.service.task.ChainTaskFilterParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class TaskRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private ChainTaskFilterParams chainTaskFilterParams;

    @InjectMocks
    private TaskRepositoryJdbc taskRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void update_shouldUpdateTask() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Updated Task");
        task.setDescription("Updated Description");
        task.setStatus(Status.COMPLETED);
        task.setPriority(Priority.LOW);
        task.setAssigneeId(3L);
        LocalDateTime updatedAt = LocalDateTime.parse("2024-12-12T20:44:42.401");
        task.setUpdatedAt(updatedAt);

        String expectedSql = """
        UPDATE entity_schema.tasks
        SET title = ?, description = ?, status = ?::status_type, priority = ?::priority_type,
            assignee_id = ?, updated_at = ?
        WHERE id = ?
    """;

        when(jdbcTemplate.update(
                eq(expectedSql),
                eq("Updated Task"),
                eq("Updated Description"),
                eq("завершено"),
                eq("низкий"),
                eq(3L),
                eq(Timestamp.valueOf(updatedAt)),
                eq(1L)
        )).thenReturn(1);

        taskRepository.update(task);

        verify(jdbcTemplate, times(1)).update(
                eq(expectedSql),
                eq("Updated Task"),
                eq("Updated Description"),
                eq("завершено"),
                eq("низкий"),
                eq(3L),
                eq(LocalDateTime.parse("2024-12-12T20:44:42.401")),
                eq(1L)
        );
    }

    @Test
    void getAll_shouldReturnListOfTasks() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");

        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(List.of(task));

        List<Task> tasks = taskRepository.getAll();

        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals("Test Task", tasks.get(0).getTitle());
        verify(jdbcTemplate, times(1)).query(anyString(), any(RowMapper.class));
    }

    @Test
    void deleteById_shouldDeleteTask() {
        when(jdbcTemplate.update(anyString(), anyLong())).thenReturn(1);

        int rowsAffected = taskRepository.deleteById(1L);

        assertEquals(1, rowsAffected);
        verify(jdbcTemplate, times(1)).update(anyString(), anyLong());
    }

    @Test
    void findById_shouldReturnTaskIfFound() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");

        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), anyLong())).thenReturn(task);

        Optional<Task> result = taskRepository.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Task", result.get().getTitle());
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), any(RowMapper.class), anyLong());
    }

    @Test
    void findById_shouldReturnEmptyIfNotFound() {
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), anyLong())).thenThrow(EmptyResultDataAccessException.class);

        Optional<Task> result = taskRepository.findById(1L);

        assertFalse(result.isPresent());
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), any(RowMapper.class), anyLong());
    }
}

