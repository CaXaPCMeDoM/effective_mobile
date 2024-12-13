package org.effective.mobile.core.repository;

import org.effective.mobile.core.entity.Comment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentRepositoryJdbc implements CommentRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Comment> commentRowMapper;

    public CommentRepositoryJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.commentRowMapper = BeanPropertyRowMapper.newInstance(Comment.class);
    }

    public Comment create(Comment comment) {
        String sql = """
                INSERT INTO entity_schema.comments (task_id, author_id, content)
                VALUES (?, ?, ?)
                RETURNING *
                """;
        return jdbcTemplate.queryForObject(
                sql,
                commentRowMapper,
                comment.getTaskId(),
                comment.getAuthorId(),
                comment.getContent()
        );
    }

    public List<Comment> findAll() {
        String sql = "SELECT * FROM entity_schema.comments";
        return jdbcTemplate.query(sql, commentRowMapper);
    }

    public List<Comment> findByTaskId(Long taskId) {
        String sql = "SELECT * FROM entity_schema.comments WHERE task_id = ?";
        return jdbcTemplate.query(sql, commentRowMapper, taskId);
    }

    public int deleteById(Long commentId) {
        String sql = "DELETE FROM entity_schema.comments WHERE id = ?";
        return jdbcTemplate.update(sql, commentId);
    }

    public boolean existsById(Long commentId) {
        String sql = "SELECT COUNT(*) FROM entity_schema.comments WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, commentId);
        return count != null && count > 0;
    }
}

