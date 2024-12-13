package org.effective.mobile.core.service.task.chain;

import org.effective.mobile.core.entity.TaskFilterParams;
import org.effective.mobile.core.service.task.TaskFilterParamsHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthorFilter extends TaskFilterParamsHandler {
    @Override
    public void handle(TaskFilterParams taskFilterParams, StringBuilder sql, List<Object> params) {
        taskFilterParams.getAuthorId().ifPresent(authorId -> {
            sql.append(" AND author_id = ?");
            params.add(authorId);
            if (nextHandler != null) {
                nextHandler.handle(taskFilterParams, sql, params);
            }
        });
    }
}
