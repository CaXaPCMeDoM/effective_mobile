package org.effective.mobile.core.repository;

import org.effective.mobile.core.entity.Task;
import org.effective.mobile.core.entity.TaskFilterParams;
import org.effective.mobile.core.entity.enums.Priority;
import org.effective.mobile.core.entity.enums.Status;
import org.effective.mobile.core.exception.TaskNotFoundException;
import org.effective.mobile.core.service.task.ChainTaskFilterParams;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TaskRepositoryJdbc implements TaskRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Task> taskRowMapper;
    private final ChainTaskFilterParams chainTaskFilterParams;

    public TaskRepositoryJdbc(JdbcTemplate jdbcTemplate, ChainTaskFilterParams chainTaskFilterParams) {
        this.jdbcTemplate = jdbcTemplate;
        this.chainTaskFilterParams = chainTaskFilterParams;
        this.taskRowMapper = (rs, rowNum) -> Task.builder()
                .id(rs.getLong("id"))
                .title(rs.getString("title"))
                .description(rs.getString("description"))
                .status(Status.fromString(rs.getString("status")))
                .priority(Priority.fromString(rs.getString("priority")))
                .authorId(rs.getLong("author_id"))
                .assigneeId(rs.getLong("assignee_id"))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                .build();
    }

    public Task create(Task task) {
        String sql = """
                INSERT INTO entity_schema.tasks
                (title, description, status, priority, author_id, assignee_id, created_at, updated_at)
                VALUES (?, ?, ?::status_type, ? :: priority_type, ?, ?, ?, ?)
                RETURNING id
                """;
        Long id = jdbcTemplate.queryForObject(
                sql,
                Long.class,
                task.getTitle(),
                task.getDescription(),
                task.getStatus().getValue(),
                task.getPriority().getValue(),
                task.getAuthorId(),
                task.getAssigneeId(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
        task.setId(id);
        return task;
    }

    public void update(Task task) {
        String sql = """
                    UPDATE entity_schema.tasks
                    SET title = ?, description = ?, status = ?::status_type, priority = ?::priority_type,
                        assignee_id = ?, updated_at = ?
                    WHERE id = ?
                """;
        jdbcTemplate.update(
                sql,
                task.getTitle(),
                task.getDescription(),
                task.getStatus().getValue(),
                task.getPriority().getValue(),
                task.getAssigneeId(),
                task.getUpdatedAt(),
                task.getId()
        );
    }

    public List<Task> getAll() {
        String sql = "SELECT * FROM entity_schema.tasks";
        return jdbcTemplate.query(sql, taskRowMapper);
    }

    public int deleteById(Long id) throws TaskNotFoundException {
        String sql = "DELETE FROM entity_schema.tasks WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        if (rowsAffected == 0) {
            throw new TaskNotFoundException();
        }
        return rowsAffected;
    }

    public Optional<Task> findById(Long id) {
        String sql = "SELECT * FROM entity_schema.tasks WHERE id = ?";
        try {
            Task task = jdbcTemplate.queryForObject(sql, taskRowMapper, id);
            return Optional.ofNullable(task);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Task> getTaskWithFilterByTaskFilterParams(TaskFilterParams taskFilterParams) {
        StringBuilder sql = new StringBuilder("SELECT * FROM entity_schema.tasks WHERE 1=1");
        List<Object> params = new ArrayList<>();

        chainTaskFilterParams.assemblingTheChain(taskFilterParams, sql, params);

        String filterSql = sql.toString();

        return jdbcTemplate.query(filterSql, taskRowMapper, params.toArray());
    }
}