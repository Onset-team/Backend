package com.stoov.review.dto;

import com.stoov.review.entity.Review;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
public record ReviewListResponse(
        Long reviewId,
        String nickname,
        String content,
        boolean isMyReview,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
    public static ReviewListResponse of(Review review, UUID currentUserId) {
        boolean isMyReview = review.getUser().getId().equals(currentUserId);
        return ReviewListResponse.builder()
                .reviewId(review.getId())
                .nickname(review.getUser().getMaskedNickname())
                .content(review.getContent())
                .isMyReview(isMyReview)
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }

    public static ReviewListResponse of(Review review) {
        return ReviewListResponse.builder()
                .reviewId(review.getId())
                .nickname(review.getUser().getMaskedNickname())
                .content(review.getContent())
                .isMyReview(false)
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }
}
