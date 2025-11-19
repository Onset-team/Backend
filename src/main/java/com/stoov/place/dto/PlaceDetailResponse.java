package com.stoov.place.dto;

import com.stoov.place.entity.Place;
import lombok.Builder;

import java.math.BigDecimal;

public record PlaceDetailResponse(
	Long placeId,
	String name,
	String address,
	BigDecimal lat,
	BigDecimal lng,
	String type,
	String contact,
	String reservationUrl,
	String operatingTime,
	String availableDays,
	String fee,
	String maxPerformers,
	String howToApply,
	Boolean electricityAvailable,
	String thumbnailUrl,
	boolean bookmarked
) {

	@Builder
	public PlaceDetailResponse {
	}

	public static PlaceDetailResponse of(Place place, boolean bookmarked) {
		return PlaceDetailResponse.builder()
			.placeId(place.getId())
			.name(place.getName())
			.address(place.getAddress())
			.lat(place.getLat())
			.lng(place.getLng())
			.type(place.getType().name()) // Assuming PlaceType is an enum
			.contact(place.getContact())
			.reservationUrl(place.getReservationUrl())
			.operatingTime(place.getOperationTime())
			.availableDays(place.getAvilableDays())
			.fee(place.getFee().name()) // Assuming FeeType is an enum
			.maxPerformers(place.getMaxPerformers().name()) // Assuming MaxPerformersType is an enum
			.howToApply(place.getHowToApply())
			.electricityAvailable(place.getElectricityAvailable())
			.thumbnailUrl(place.getThumbnailUrl())
			.bookmarked(bookmarked)
			.build();
	}
}

