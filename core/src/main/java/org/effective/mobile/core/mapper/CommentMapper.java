package org.effective.mobile.core.mapper;

import org.effective.mobile.core.dto.CommentRequest;
import org.effective.mobile.core.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {
    Comment toComment(CommentRequest request);

    List<Comment> toComments(List<CommentRequest> requests);
}
