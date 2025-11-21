package com.stoov.review.dto;

import com.stoov.review.entity.Review;
import lombok.Builder;

import java.util.UUID;

@Builder
public record ReviewListResponse(
        Long reviewId,
        String nickname,
        String content,
        boolean isMyReview
) {
    public static ReviewListResponse of(Review review, UUID currentUserId) {
        boolean isMyReview = review.getUser().getId().equals(currentUserId);
        return ReviewListResponse.builder()
                .reviewId(review.getId())
                .nickname(review.getUser().getMaskedNickname())
                .content(review.getContent())
                .isMyReview(isMyReview)
                .build();
    }

    public static ReviewListResponse of(Review review) {
        return ReviewListResponse.builder()
                .reviewId(review.getId())
                .nickname(review.getUser().getMaskedNickname())
                .content(review.getContent())
                .isMyReview(false)
                .build();
    }
}
