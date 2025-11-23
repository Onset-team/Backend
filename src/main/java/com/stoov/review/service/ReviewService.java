package com.stoov.review.service;

import com.stoov.common.exception.BusinessException;
import com.stoov.common.exception.ErrorCode;
import com.stoov.place.entity.Place;
import com.stoov.place.repository.PlaceRepository;
import com.stoov.review.dto.ReviewCreateRequest;
import com.stoov.review.dto.ReviewCreateResponse;
import com.stoov.review.dto.ReviewUpdateResponse;
import com.stoov.review.dto.ReviewListResponse;
import com.stoov.review.dto.ReviewUpdateRequest;
import com.stoov.review.entity.Review;
import com.stoov.review.repository.ReviewRepository;
import com.stoov.user.entity.User;
import com.stoov.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    /**
     * 리뷰 작성
     */
    @Transactional
    public ReviewCreateResponse createReview(Long placeId, UUID userId, ReviewCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PLACE_NOT_FOUND));

        Review review = Review.builder()
                .user(user)
                .place(place)
                .content(request.content())
                .build();

        Review savedReview = reviewRepository.save(review);

        return ReviewCreateResponse.of(savedReview);
    }

    /**
     * 리뷰 목록 조회
     */
    @Transactional(readOnly = true)
    public List<ReviewListResponse> getReviews(Long placeId, UUID userId) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PLACE_NOT_FOUND));

        List<Review> reviews = reviewRepository.findAllByPlaceOrderByCreatedAtDesc(place);

        if (userId == null) {
            return reviews.stream()
                    .map(ReviewListResponse::of)
                    .collect(Collectors.toList());
        }

        return reviews.stream()
                .map(review -> ReviewListResponse.of(review, userId))
                .collect(Collectors.toList());
    }

    /**
     * 리뷰 수정
     */
    @Transactional
    public ReviewUpdateResponse updateReview(Long reviewId, UUID userId, ReviewUpdateRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_FOUND));

        if (!review.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        review.updateContent(request.content());

        return ReviewUpdateResponse.of(review);
    }

    /**
     * 리뷰 삭제
     */
    @Transactional
    public void deleteReview(Long reviewId, UUID userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_FOUND));

        if (!review.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        reviewRepository.delete(review);
    }
}
