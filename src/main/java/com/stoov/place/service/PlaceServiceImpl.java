package com.stoov.place.service;

import com.stoov.place.dto.PlaceResponse;
import com.stoov.place.entity.Place;
import com.stoov.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {

    private final PlaceRepository placeRepository;

    @Override
    public List<PlaceResponse> getAllPlaces(UUID userId) {
        List<Place> places = placeRepository.findAll();

        return places.stream()
                .map(place -> {
                    int reviewCount = 0;
                    // 후기 갯수 집계 로직이 들어갈 자리, 우선은 하드코딩

                    boolean bookmarked = false;
                    // 즐겨찾기 로직이 들어갈 자리, 우선은 하드코딩
                    // 세션이 있는 경우(userId != null) 로그인한 유저의 북마크 여부를 조회해서 설정

                    return PlaceResponse.from(place, reviewCount, bookmarked);
                })
                .toList();
    }
}
