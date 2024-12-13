package org.effective.mobile.core.service;

import lombok.RequiredArgsConstructor;
import org.effective.mobile.core.dto.CommentRequest;
import org.effective.mobile.core.entity.Comment;
import org.effective.mobile.core.exception.CommentNotFoundException;
import org.effective.mobile.core.mapper.CommentMapper;
import org.effective.mobile.core.repository.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
/**
 * Сервис для управления комментариями.
 */
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    /**
     * Создаёт новый комментарий.
     * @param request запрос на создание комментария
     * @return созданный комментарий
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Comment createComment(CommentRequest request) {
        LocalDateTime localDateTime = LocalDateTime.now();
        Comment comment = commentMapper.toComment(request);
        comment.setCreatedAt(localDateTime);
        return commentRepository.create(comment);
    }

    /**
     * Возвращает все комментарии.
     * @return список всех комментариев
     */
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    /**
     * Возвращает комментарии по ID задачи.
     * @param taskId ID задачи
     * @return список комментариев для указанной задачи
     */
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<Comment> getCommentsByTaskId(Long taskId) {
        return commentRepository.findByTaskId(taskId);
    }
    /**
     * Удаляет комментарий по ID.
     * @param commentId ID комментария
     * @throws CommentNotFoundException если комментарий не найден
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void deleteComment(Long commentId) throws CommentNotFoundException {
        if (!commentRepository.existsById(commentId)) {
            throw new CommentNotFoundException("Comment not found");
        }
        commentRepository.deleteById(commentId);
    }
}
