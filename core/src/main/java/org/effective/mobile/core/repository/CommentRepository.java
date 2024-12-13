package org.effective.mobile.core.repository;

import org.effective.mobile.core.entity.Comment;

import java.util.List;

public interface CommentRepository {
    Comment create(Comment comment);

    List<Comment> findAll();

    List<Comment> findByTaskId(Long taskId);

    int deleteById(Long commentId);

    boolean existsById(Long commentId);
}
