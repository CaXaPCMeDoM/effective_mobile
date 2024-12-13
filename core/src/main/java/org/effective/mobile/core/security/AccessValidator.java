package org.effective.mobile.core.security;

import lombok.RequiredArgsConstructor;
import org.effective.mobile.core.entity.Task;
import org.effective.mobile.core.entity.User;
import org.effective.mobile.core.exception.TaskNotFoundException;
import org.effective.mobile.core.service.CommentService;
import org.effective.mobile.core.service.TaskService;
import org.effective.mobile.core.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AccessValidator {
    private final TaskService taskService;
    private final UserService userService;

    public boolean isTaskAccess(Long taskId) throws TaskNotFoundException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = auth.getName();

        Task task = taskService.getTaskById(taskId);
        Optional<User> user = userService.getByEmail(currentUserEmail);
        return user.filter(
                u -> u.getId().equals(task.getAssigneeId())
        ).isPresent();
    }
}
