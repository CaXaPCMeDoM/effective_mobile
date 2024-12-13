package org.effective.mobile.core.repository;

import org.effective.mobile.core.entity.Comment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CommentRepositoryJdbcTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private CommentRepositoryJdbc commentRepositoryJdbc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_shouldInsertAndReturnComment() {
        Comment comment = new Comment();
        comment.setTaskId(1L);
        comment.setAuthorId(2L);
        comment.setContent("Test Comment");

        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), anyLong(), anyLong(), anyString()))
                .thenReturn(comment);

        Comment result = commentRepositoryJdbc.create(comment);

        assertNotNull(result);
        assertEquals("Test Comment", result.getContent());
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), any(RowMapper.class), anyLong(), anyLong(), anyString());
    }

    @Test
    void findAll_shouldReturnListOfComments() {
        Comment comment = new Comment();
        comment.setTaskId(1L);
        comment.setContent("Test Comment");

        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(List.of(comment));

        List<Comment> comments = commentRepositoryJdbc.findAll();

        assertNotNull(comments);
        assertEquals(1, comments.size());
        assertEquals("Test Comment", comments.get(0).getContent());
        verify(jdbcTemplate, times(1)).query(anyString(), any(RowMapper.class));
    }

    @Test
    void findByTaskId_shouldReturnCommentsForTask() {
        Comment comment = new Comment();
        comment.setTaskId(1L);
        comment.setContent("Task Comment");

        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(1L))).thenReturn(List.of(comment));

        List<Comment> comments = commentRepositoryJdbc.findByTaskId(1L);

        assertNotNull(comments);
        assertEquals(1, comments.size());
        assertEquals("Task Comment", comments.get(0).getContent());
        verify(jdbcTemplate, times(1)).query(anyString(), any(RowMapper.class), eq(1L));
    }

    @Test
    void deleteById_shouldDeleteComment() {
        when(jdbcTemplate.update(anyString(), anyLong())).thenReturn(1);

        int rowsAffected = commentRepositoryJdbc.deleteById(1L);

        assertEquals(1, rowsAffected);
        verify(jdbcTemplate, times(1)).update(anyString(), anyLong());
    }

    @Test
    void existsById_shouldReturnTrueIfCommentExists() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(1L))).thenReturn(1);

        boolean exists = commentRepositoryJdbc.existsById(1L);

        assertTrue(exists);
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), eq(Integer.class), eq(1L));
    }

    @Test
    void existsById_shouldReturnFalseIfCommentDoesNotExist() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(1L))).thenReturn(0);

        boolean exists = commentRepositoryJdbc.existsById(1L);

        assertFalse(exists);
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), eq(Integer.class), eq(1L));
    }
}

