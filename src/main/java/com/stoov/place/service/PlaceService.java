package com.stoov.place.service;

import com.stoov.bookmark.repository.BookmarkRepository;
import com.stoov.common.exception.BusinessException;
import com.stoov.common.exception.ErrorCode;
import com.stoov.place.dto.PlaceDetailResponse;
import com.stoov.place.dto.PlaceSearchResponse;
import com.stoov.place.entity.Place;
import com.stoov.place.repository.PlaceRepository;
import com.stoov.user.entity.User;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlaceService {
	private final PlaceRepository placeRepository;
	private final BookmarkRepository bookmarkRepository;

	/**
	 * 장소 데이터 상세 조회
	 */
	@Transactional(readOnly = true)
	public PlaceDetailResponse getPlaceDetail(Long placeId, User user) {

		Place place = placeRepository.findById(placeId)
			.orElseThrow(() -> new BusinessException(ErrorCode.PLACE_NOT_FOUND));

		boolean isBookmarked = false;
		if (user != null) {
			isBookmarked = bookmarkRepository.findByUserAndPlace(user, place).isPresent();
		}

		return PlaceDetailResponse.of(place, isBookmarked);
	}

	/**
	 * 장소 검색
	 */
	@Transactional(readOnly = true)
	public Page<PlaceSearchResponse> searchPlaces(String keyword, User user, Pageable pageable) {
		String processedKeyword = keyword.replaceAll("\\s+", "");
		Page<Place> places = placeRepository.searchByNameOrDistrict(processedKeyword, pageable);

		return places.map(place -> {
			boolean isBookmarked = false;
			if (user != null) {
				isBookmarked = bookmarkRepository.findByUserAndPlace(user, place).isPresent();
			}
			return PlaceSearchResponse.of(place, isBookmarked);
		});
	}
}


