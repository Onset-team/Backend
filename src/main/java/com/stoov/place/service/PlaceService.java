package com.stoov.place.service;

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

import java.util.List;
import java.util.UUID;

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
	 */
	@Transactional(readOnly = true)
	public Page<PlaceSearchResponse> searchPlaces(String keyword, UUID userId, Pageable pageable) {
		if (keyword == null || keyword.isBlank()) {
			return Page.empty(pageable);
		}

		String processedKeyword = keyword.replaceAll("\\s+", "");
		String searchKeyword = "%" + processedKeyword + "%";
		Page<Place> places = placeRepository.searchByNameOrDistrict(searchKeyword, pageable);

		return places.map(place -> {
            int reviewCount = (int) reviewRepository.countByPlace(place);
			boolean isBookmarked = false;
			if (userId != null) {
				User user = userRepository.findById(userId)
					.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
				isBookmarked = bookmarkRepository.findByUserAndPlace(user, place).isPresent();
			}

			return PlaceSearchResponse.of(place, reviewCount, isBookmarked);
		});
	}

    /**
     * 장소 목록 조회
     */
    @Transactional(readOnly = true) // 추가
    public List<PlaceResponse> getAllPlaces(UUID userId) {
        List<Place> places = placeRepository.findAll();
        User user = null;

        if (userId != null) {
            user = userRepository.findById(userId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        }
        final User resolvedUser = user;
        return places.stream()
                .map(place -> {
                    int reviewCount = (int) reviewRepository.countByPlace(place); // 수정

                    boolean isBookmarked = false;
                    if (resolvedUser != null) {
                        isBookmarked = bookmarkRepository.findByUserAndPlace(resolvedUser,place).isPresent();
                    }

                    return PlaceResponse.from(place, reviewCount, isBookmarked);
                })
                .toList();
    }
}
