package com.stoov.review.dto;

import com.stoov.review.entity.Review;
import lombok.Builder;

@Builder
public record ReviewCreateResponse(
    Long reviewId,
    String nickname,
    String content
) {
    public static ReviewCreateResponse of(Review review) {
        return ReviewCreateResponse.builder()
            .reviewId(review.getId())
            .nickname(review.getUser().getMaskedNickname())
            .content(review.getContent())
            .build();
    }
}
