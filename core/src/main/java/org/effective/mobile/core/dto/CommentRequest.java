package org.effective.mobile.core.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentRequest {

    @NotNull
    private Long taskId;

    @NotNull
    private Long authorId;

    @NotBlank
    private String content;
}