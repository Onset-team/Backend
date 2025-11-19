package com.stoov.place.controller;

import com.stoov.common.dto.PageResponse;
import com.stoov.place.dto.PlaceResponse;
import com.stoov.user.helper.UserResolver;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stoov.common.response.CustomApiResponse;
import com.stoov.place.dto.PlaceDetailResponse;
import com.stoov.place.dto.PlaceSearchResponse;
import com.stoov.place.service.PlaceService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/places")
public class PlaceController {

	private final PlaceService placeService;
	private final UserResolver userResolver;

	/**
	 * 장소 상세 정보 조회 API
	 * [GET] /api/places/{placeId}
	 */
	@GetMapping("/{placeId}")
	public ResponseEntity<CustomApiResponse<PlaceDetailResponse>> getPlaceDetail(
		@PathVariable Long placeId,
		HttpServletRequest request) {

		UUID userId = userResolver.resolveUserId(request);
		PlaceDetailResponse placeDetail = placeService.getPlaceDetail(placeId, userId);
		return ResponseEntity.ok(CustomApiResponse.success(placeDetail));
	}

	/**
	 * 장소 검색 API
	 * [GET] /api/places/search
	 */
	@GetMapping("/search")
	public ResponseEntity<CustomApiResponse<PageResponse<PlaceSearchResponse>>> searchPlaces(
		@RequestParam String keyword,
		@RequestParam(defaultValue = "10") int blockSize,
		HttpServletRequest request,
		Pageable pageable) {

		UUID userId = userResolver.resolveUserId(request);
		Page<PlaceSearchResponse> searchResults = placeService.searchPlaces(keyword, userId, pageable);
		PageResponse<PlaceSearchResponse> pageResponse = new PageResponse<>(searchResults, blockSize);
		return ResponseEntity.ok(CustomApiResponse.success(pageResponse));
	}

    @GetMapping
    public ResponseEntity<CustomApiResponse<List<PlaceResponse>>> getAllPlaces(
            HttpServletRequest request) {

        UUID userId = userResolver.resolveUserId(request);
        List<PlaceResponse> response = placeService.getAllPlaces(userId);
        return ResponseEntity.ok(CustomApiResponse.success(response));
    }
}


