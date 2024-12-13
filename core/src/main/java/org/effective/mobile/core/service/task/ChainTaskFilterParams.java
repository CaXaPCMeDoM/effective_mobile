package org.effective.mobile.core.service.task;

import org.effective.mobile.core.entity.TaskFilterParams;
import org.effective.mobile.core.service.task.chain.AssigneeFilter;
import org.effective.mobile.core.service.task.chain.AuthorFilter;
import org.effective.mobile.core.service.task.chain.PriorityFilter;
import org.effective.mobile.core.service.task.chain.StatusFilter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Реализация классического паттерна "Цепочка обязанностей".
 * В данном случае используется для добавления к sql запросу нужных фильтров.
 * Чтобы цепочка сработала, нужно вызывать assemblingTheChain
 */
@Component
public class ChainTaskFilterParams {
    private final TaskFilterParamsHandler taskFilterParamsHandler;

    /**
     * Конструктор для создания цепочки фильтров.
     * Добавь новые фильтры при необходимости.
     * @param assigneeFilter фильтр по исполнителю
     * @param authorFilter фильтр по автору
     * @param priorityFilter фильтр по приоритету
     * @param statusFilter фильтр по статусу
     */
    public ChainTaskFilterParams(AssigneeFilter assigneeFilter,
                                 AuthorFilter authorFilter,
                                 PriorityFilter priorityFilter,
                                 StatusFilter statusFilter) {
        assigneeFilter.setNextHandler(
                authorFilter.setNextHandler(
                        priorityFilter.setNextHandler(
                                statusFilter
                        )
                )
        );
        this.taskFilterParamsHandler =assigneeFilter;
    }

    /**
     * Собирает цепочку фильтров и применяет их к SQL запросу.
     * @param taskFilterParams параметры фильтрации задач
     * @param sql объект {@code StringBuilder}, содержащий SQL запрос
     * @param params список параметров для SQL запроса
     */
    public void assemblingTheChain(TaskFilterParams taskFilterParams, StringBuilder sql, List<Object> params) {
        taskFilterParamsHandler.handle(taskFilterParams, sql, params);
    }
}
