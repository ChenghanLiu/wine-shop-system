package com.wineshop.comment.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentCreateRequest {
    @NotNull
    private Long orderItemId;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer score;

    @NotBlank
    private String content;
}
