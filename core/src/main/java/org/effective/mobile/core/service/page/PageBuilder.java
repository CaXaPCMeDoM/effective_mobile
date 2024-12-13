package org.effective.mobile.core.service.page;

import org.springframework.data.domain.Page;

import java.util.List;

public interface PageBuilder<T> {
    Page<T> buildPage(List<T> items, int page, int size);
}
