package com.stoov.review.dto;

import com.stoov.review.entity.Review;
import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record ReviewCreateResponse(
    Long reviewId,
    String nickname,
    String content,
    OffsetDateTime createdAt
) {
    public static ReviewCreateResponse of(Review review) {
        return ReviewCreateResponse.builder()
            .reviewId(review.getId())
            .nickname(review.getUser().getMaskedNickname())
            .content(review.getContent())
            .createdAt(review.getCreatedAt())
            .build();
    }
}
