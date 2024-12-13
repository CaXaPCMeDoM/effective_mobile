package org.effective.mobile.core.service.task.chain;

import org.effective.mobile.core.entity.TaskFilterParams;
import org.effective.mobile.core.service.task.TaskFilterParamsHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PriorityFilter extends TaskFilterParamsHandler {
    @Override
    public void handle(TaskFilterParams taskFilterParams, StringBuilder sql, List<Object> params) {
        taskFilterParams.getPriority().ifPresent(priority -> {
            sql.append(" AND priority = ?::priority_type");
            params.add(priority);
            if (nextHandler != null) {
                nextHandler.handle(taskFilterParams, sql, params);
            }
        });
    }
}
