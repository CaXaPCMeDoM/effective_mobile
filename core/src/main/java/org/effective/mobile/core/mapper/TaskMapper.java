package org.effective.mobile.core.mapper;

import org.effective.mobile.core.dto.TaskRequest;
import org.effective.mobile.core.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TaskMapper {
    void updateTaskFromRequest(TaskRequest taskRequest, @MappingTarget Task task);
}