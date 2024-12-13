package org.effective.mobile.core.util;

import org.effective.mobile.core.entity.enums.Priority;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PriorityConverter implements Converter<String, Priority> {
    @Override
    public Priority convert(String source) {
        if (source.isEmpty()) {
            return null;
        }
        for (Priority priority : Priority.values()) {
            if (priority.getValue().equalsIgnoreCase(source)) {
                return priority;
            }
        }
        throw new IllegalArgumentException("Unknown priority: " + source);
    }
}
