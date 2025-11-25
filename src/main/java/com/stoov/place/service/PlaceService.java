package com.stoov.place.service;

import com.stoov.bookmark.entity.Bookmark;
import com.stoov.bookmark.repository.BookmarkRepository;
import com.stoov.common.exception.BusinessException;
import com.stoov.common.exception.ErrorCode;
import com.stoov.place.dto.PlaceDetailResponse;
import com.stoov.place.dto.PlaceResponse;
import com.stoov.place.dto.PlaceSearchResponse;
import com.stoov.place.entity.Place;
import com.stoov.place.repository.PlaceRepository;
import com.stoov.review.repository.ReviewRepository;
import com.stoov.user.entity.User;
import com.stoov.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceService {
	private final PlaceRepository placeRepository;
	private final BookmarkRepository bookmarkRepository;
	private final UserRepository userRepository;
	private final ReviewRepository reviewRepository;

	/**
	 * 장소 데이터 상세 조회
	 */
	@Transactional(readOnly = true)
	public PlaceDetailResponse getPlaceDetail(Long placeId, UUID userId) {

		Place place = placeRepository.findById(placeId)
			.orElseThrow(() -> new BusinessException(ErrorCode.PLACE_NOT_FOUND));

		boolean isBookmarked = false;
		if (userId != null) {
			User user = userRepository.findById(userId)
				.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
			isBookmarked = bookmarkRepository.findByUserAndPlace(user, place).isPresent();
		}

		return PlaceDetailResponse.of(place, isBookmarked);
	}

	/**
	 * 장소 검색
	 * 성능 개선 O, 검색 시 N+1 제거
	 */
	@Transactional(readOnly = true)
	public Page<PlaceSearchResponse> searchPlaces(String keyword, UUID userId, Pageable pageable) {

		if (keyword == null || keyword.isBlank()) {
			return Page.empty(pageable);
		}

		String processedKeyword = keyword.replaceAll("\\s+", "");
		String searchKeyword = "%" + processedKeyword + "%";

		// 1) 검색 결과 페이지 조회
		Page<Place> placePage = placeRepository.searchByNameOrDistrict(searchKeyword, pageable);
		List<Place> placeList = placePage.getContent();

		if (placeList.isEmpty()) {
			// 내용이 없다면 기본값으로 매핑
			return placePage.map(place -> PlaceSearchResponse.of(place, 0, false));
		}

		// 2) 사용자 조회 (있을 때만)
		User user = null;
		if (userId != null) {
			user = userRepository.findById(userId)
				.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
		}

		// 3) 리뷰 개수 일괄 조회
		List<Object[]> reviewCountRows = reviewRepository.countByPlaces(placeList);
		Map<Long, Long> reviewCountMap = reviewCountRows.stream()
			.collect(Collectors.toMap(
				row -> (Long) row[0],
				row -> (Long) row[1]
			));

		// 4) 북마크 일괄 조회
		Set<Long> bookmarkedPlaceIds = Collections.emptySet();
		if (user != null) {
			List<Bookmark> bookmarks = bookmarkRepository.findByUserAndPlaceIn(user, placeList);
			bookmarkedPlaceIds = bookmarks.stream()
				.map(bookmark -> bookmark.getPlace().getId())
				.collect(Collectors.toSet());
		}

		// 람다에서 사용하기 위한 final 변수
		final Map<Long, Long> finalReviewCountMap = reviewCountMap;
		final Set<Long> finalBookmarkedPlaceIds = bookmarkedPlaceIds;

		// 5) Page<Place> → Page<PlaceSearchResponse> 매핑
		return placePage.map(place -> {
			int reviewCount = finalReviewCountMap
				.getOrDefault(place.getId(), 0L)
				.intValue();
			boolean isBookmarked = finalBookmarkedPlaceIds.contains(place.getId());
			return PlaceSearchResponse.of(place, reviewCount, isBookmarked);
		});
	}

	/**
	 * 장소 목록 조회
	 * 성능 개선, 장소 개수와 상관없이 쿼리 3번으로 고정
	 */
	@Transactional(readOnly = true) // 추가
	public List<PlaceResponse> getAllPlaces(UUID userId) {

		// 1) 전체 장소 조회
		List<Place> placeList = placeRepository.findAll();
		if (placeList.isEmpty()) {
			return List.of();
		}

		// 2) 사용자 조회 (있을 때만)
		User user = null;
		if (userId != null) {
			user = userRepository.findById(userId)
				.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
		}

		// 3) 리뷰 개수 한 번에 조회 → Map<placeId, count>
		List<Object[]> reviewCountRows = reviewRepository.countByPlaces(placeList);
		Map<Long, Long> reviewCountMap = reviewCountRows.stream()
			.collect(Collectors.toMap(
				row -> (Long) row[0], // placeId
				row -> (Long) row[1]  // count
			));

		// 4) 북마크 한 번에 조회 → Set<placeId>
		Set<Long> bookmarkedPlaceIds = Collections.emptySet();
		if (user != null) {
			List<Bookmark> bookmarks = bookmarkRepository.findByUserAndPlaceIn(user, placeList);
			bookmarkedPlaceIds = bookmarks.stream()
				.map(bookmark -> bookmark.getPlace().getId())
				.collect(Collectors.toSet());
		}

		// 람다에서 쓸 수 있도록 final 변수로 한 번 더 래핑
		final Map<Long, Long> finalReviewCountMap = reviewCountMap;
		final Set<Long> finalBookmarkedPlaceIds = bookmarkedPlaceIds;

		// 5) 최종 응답 변환
		return placeList.stream()
			.map(place -> {
				int reviewCount = finalReviewCountMap
					.getOrDefault(place.getId(), 0L)
					.intValue();
				boolean isBookmarked = finalBookmarkedPlaceIds.contains(place.getId());
				return PlaceResponse.from(place, reviewCount, isBookmarked);
			})
			.toList();
	}
}