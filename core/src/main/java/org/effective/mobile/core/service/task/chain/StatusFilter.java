package org.effective.mobile.core.service.task.chain;

import org.effective.mobile.core.entity.TaskFilterParams;
import org.effective.mobile.core.service.task.TaskFilterParamsHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StatusFilter extends TaskFilterParamsHandler {
    @Override
    public void handle(TaskFilterParams taskFilterParams, StringBuilder sql, List<Object> params) {
        taskFilterParams.getStatus().ifPresent(status -> {
            sql.append(" AND status = ?::status_type");
            params.add(status);
            if (nextHandler != null) {
                nextHandler.handle(taskFilterParams, sql, params);
            }
        });
    }
}
