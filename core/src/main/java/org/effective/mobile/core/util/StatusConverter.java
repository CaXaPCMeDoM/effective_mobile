package org.effective.mobile.core.util;

import org.effective.mobile.core.entity.enums.Status;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StatusConverter implements Converter<String, Status> {
    @Override
    public Status convert(String source) {
        if (source.isEmpty()) {
            return null;
        }
        for (Status status : Status.values()) {
            if (status.getValue().equalsIgnoreCase(source)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + source);
    }
}
