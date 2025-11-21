package com.stoov.review.controller;

import com.stoov.common.response.CustomApiResponse;
import com.stoov.review.dto.ReviewCreateResponse;
import com.stoov.review.dto.ReviewUpdateRequest;
import com.stoov.review.service.ReviewService;
import com.stoov.user.helper.UserResolver;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserResolver userResolver;

    /**
     * 후기 수정 API
     * @param reviewId
     * @param requestDto
     * @param request
     * @return
     */
    @PatchMapping("/{reviewId}")
    public ResponseEntity<CustomApiResponse<ReviewCreateResponse>> updateReview(
            @PathVariable Long reviewId,
            @RequestBody @Valid ReviewUpdateRequest requestDto,
            HttpServletRequest request) {
        UUID userId = userResolver.resolveRequiredUserId(request);
        ReviewCreateResponse response = reviewService.updateReview(reviewId, userId, requestDto);
        return ResponseEntity.ok(CustomApiResponse.success(response));
    }

    /**
     * 후기 삭제 API
     * @param reviewId
     * @param request
     * @return
     */
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<CustomApiResponse<Void>> deleteReview(
            @PathVariable Long reviewId,
            HttpServletRequest request) {
        UUID userId = userResolver.resolveRequiredUserId(request);
        reviewService.deleteReview(reviewId, userId);
        return ResponseEntity.ok(CustomApiResponse.success(null));
    }
}
