package com.stoov.bookmark.dto;

import com.stoov.place.entity.Place;
import lombok.Builder;

import java.math.BigDecimal;

public record BookmarkResponse(
    Long placeId,
    String name,
    BigDecimal lat,
    BigDecimal lng,
    String address,
    String type,
    long reviewCount,
    String thumbnailUrl,
    boolean bookmarked
) {

    @Builder
    public BookmarkResponse {
    }

    public static BookmarkResponse of(Place place, long reviewCount) {
        return BookmarkResponse.builder()
            .placeId(place.getId())
            .name(place.getName())
            .lat(place.getLat())
            .lng(place.getLng())
            .address(place.getAddress())
            .type(place.getType().getValue())
            .reviewCount(reviewCount)
            .thumbnailUrl(place.getThumbnailUrl())
            .bookmarked(true)
            .build();
    }
}
