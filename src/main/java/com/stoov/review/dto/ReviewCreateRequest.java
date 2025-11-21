package com.stoov.review.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ReviewCreateRequest(

    @Size(max = 1000)
    @NotBlank(message = "후기 내용은 비워둘 수 없습니다.")
    String content
) {
}
