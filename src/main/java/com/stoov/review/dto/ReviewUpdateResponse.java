package com.stoov.review.dto;

import com.stoov.review.entity.Review;
import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record ReviewUpdateResponse(
	Long reviewId,
	String nickname,
	String content,
	OffsetDateTime createdAt,
	OffsetDateTime updatedAt
) {
	public static ReviewUpdateResponse of(Review review) {
		return ReviewUpdateResponse.builder()
			.reviewId(review.getId())
			.nickname(review.getUser().getMaskedNickname())
			.content(review.getContent())
			.createdAt(review.getCreatedAt())
			.updatedAt(review.getUpdatedAt())
			.build();
	}
}
