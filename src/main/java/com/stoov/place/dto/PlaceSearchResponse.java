package com.stoov.place.dto;

import com.stoov.place.entity.Place;
import lombok.Builder;

import java.math.BigDecimal;

public record PlaceSearchResponse(
	Long placeId,
	String name,
	BigDecimal lat,
	BigDecimal lng,
	String address,
	String type,
	String thumbnailUrl,
	boolean bookmarked
) {

	@Builder
	public PlaceSearchResponse {
	}

	public static PlaceSearchResponse of(Place place, boolean bookmarked) {
		return PlaceSearchResponse.builder()
			.placeId(place.getId())
			.name(place.getName())
			.lat(place.getLat())
			.lng(place.getLng())
			.address(place.getAddress())
			.type(place.getType().name())
			.thumbnailUrl(place.getThumbnailUrl())
			.bookmarked(bookmarked)
			.build();
	}
}
