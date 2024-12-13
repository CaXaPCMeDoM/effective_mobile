package org.effective.mobile.core.entity;

import lombok.*;
import org.effective.mobile.core.entity.enums.Priority;
import org.effective.mobile.core.entity.enums.Status;

import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskFilterParams {
    @Builder.Default
    private Optional<Long> authorId = Optional.empty();
    @Builder.Default
    private Optional<Long> assigneeId = Optional.empty();
    @Builder.Default
    private Optional<Status> status = Optional.empty();
    @Builder.Default
    private Optional<Priority> priority = Optional.empty();
}
