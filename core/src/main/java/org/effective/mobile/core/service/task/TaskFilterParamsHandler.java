package org.effective.mobile.core.service.task;

import org.effective.mobile.core.entity.TaskFilterParams;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public abstract class TaskFilterParamsHandler {
    protected TaskFilterParamsHandler nextHandler;
    public TaskFilterParamsHandler setNextHandler(TaskFilterParamsHandler nextHandler) {
        this.nextHandler = nextHandler;
        return this;
    }
    public abstract void handle(TaskFilterParams taskFilterParams, StringBuilder sql, List<Object> params);
}
