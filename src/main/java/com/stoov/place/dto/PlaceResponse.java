package com.stoov.place.dto;

import com.stoov.place.entity.Place;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class PlaceResponse {
    private Long placeId;
    private String name;
    private BigDecimal lat;
    private BigDecimal lng;
    private String address;
    private String type;
    private int reviewCount;
    private String thumbnailUrl;
    private boolean bookmarked;

    public static PlaceResponse from(Place place, int reviewCount, boolean bookmarked) {
        return PlaceResponse.builder()
                .placeId(place.getId())
                .name(place.getName())
                .lat(place.getLat())
                .lng(place.getLng())
                .address(place.getAddress())
                .type(place.getType().getValue())
                .reviewCount(reviewCount)
                .thumbnailUrl(place.getThumbnailUrl())
                .bookmarked(bookmarked)
                .build();
    }
}
