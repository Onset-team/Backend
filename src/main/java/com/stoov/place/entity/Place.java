package com.stoov.place.entity;

import com.stoov.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Place extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "place_id", nullable = false, updatable = false)
	private Long id;

	//장소명
	@Column(name = "name", nullable = false)
	private String name;

	//도로명주소
	@Column(name = "address", nullable = false)
	private String address;

	//마포구, 강서구 등..
	@Column(name = "district", nullable = false)
	private String district;

	//위도
	@Column(name = "lat", precision = 9, scale = 6, nullable = false)
	private BigDecimal lat;

	//경도
	@Column(name = "lng", precision = 9, scale = 6, nullable = false)
	private BigDecimal lng;

	// Point 타입 필드
	@Column(columnDefinition = "geography(Point, 4326)")
	private Point location;

	//장소 유형. ENUM 확정
	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false, length = 20)
	private PlaceType type;  //RIVER_PARK | SQUARE | STREET

	//문의처
	@Column(name = "contact")
	private String contact;

	//예약 URL
	@Column(name = "reservation_url")
	private String reservationUrl;

	//운영 시간
	@Column(name = "operation_time")
	private String operationTime;

	//이용 가능 요일
	@Column(name = "avilable_days")
	private String avilableDays;

	//이용 요금
	@Enumerated(EnumType.STRING)
	@Column(name = "fee", nullable = false, length = 10)
	private FeeType fee;  // FREE | PAID | UNKNOWN

	//최대 가능 공연 인원 수
	@Enumerated(EnumType.STRING)
	@Column(name = "max_performers", nullable = false, length = 15)
	private MaxPerformersType maxPerformers;  // LIMITED | UNLIMITED | UNKNOWN

	//신청방법
	@Column(name = "how_to_apply")
	private String howToApply;

	//전기 사용
	@Column(name = "electricity_available")
	private Boolean electricityAvailable;

	//썸네일 이미지 URL
	@Column(name = "thumbnail_url")
	private String thumbnailUrl;
}

