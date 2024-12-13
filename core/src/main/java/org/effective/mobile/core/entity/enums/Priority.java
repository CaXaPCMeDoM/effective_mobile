package org.effective.mobile.core.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum Priority {
    LOW("низкий"),
    MEDIUM("средний"),
    HIGH("высокий");

    private final String value;

    Priority(String value) {
        this.value = value;
    }

    @JsonCreator
    public static Priority fromString(String value) {
        for (Priority priority : Priority.values()) {
            if (priority.value.equals(value)) {
                return priority;
            }
        }
        throw new IllegalArgumentException("Unknown priority: " + value);
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
