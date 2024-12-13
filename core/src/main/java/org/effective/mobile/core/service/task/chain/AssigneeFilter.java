package org.effective.mobile.core.service.task.chain;

import org.effective.mobile.core.entity.TaskFilterParams;
import org.effective.mobile.core.service.task.TaskFilterParamsHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AssigneeFilter extends TaskFilterParamsHandler {
    @Override
    public void handle(TaskFilterParams taskFilterParams, StringBuilder sql, List<Object> params) {
        taskFilterParams.getAssigneeId().ifPresent(assigneeId -> {
            sql.append(" AND assignee_id=?");
            params.add(assigneeId);
            if (nextHandler != null) {
                nextHandler.handle(taskFilterParams, sql, params);
            }
        });
    }
}
