package org.effective.mobile.core.service.page;

import org.effective.mobile.core.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class TaskPageBuilder implements PageBuilder<Task> {
    @Override
    public Page<Task> buildPage(List<Task> items, int page, int size) {
        int start = page * size;
        int end = Math.min(start + size, items.size());
        if (start > items.size()) {
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(page, size), items.size());
        }
        return new PageImpl<>(items.subList(start, end), PageRequest.of(page, size), items.size());
    }
}
