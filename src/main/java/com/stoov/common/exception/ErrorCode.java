package com.stoov.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {

	// Common
	COMMON_BAD_REQUEST(HttpStatus.BAD_REQUEST, "C001", "Bad Request"),
	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C002", "Invalid Input Value"),
	INVALID_REQUEST_PARAM(HttpStatus.BAD_REQUEST, "C003", "Invalid Request Parameter"),
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "C004", "Unauthorized"),
	FORBIDDEN(HttpStatus.FORBIDDEN, "C005", "Forbidden"),
	NOT_FOUND_URL(HttpStatus.NOT_FOUND, "C006", "Not Found URL"),
	NOT_FOUND(HttpStatus.NOT_FOUND, "C007", "Resource Not Found"),
	TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "C008", "Too Many Requests"),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C009", "Internal Server Error"),
	KAKAO_API_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "R004", "카카오 API 서비스 오류가 발생했습니다"),

	//Batch
	CONFLICT(HttpStatus.CONFLICT, "B001", "이미 실행 중입니다."),

	//Place
	PLACE_NOT_FOUND(HttpStatus.NOT_FOUND, "P001", "해당 장소를 찾을 수 없습니다."),

	//Bookmark
	ALREADY_BOOKMARKED(HttpStatus.CONFLICT, "BM001", "이미 즐겨찾기에 추가된 장소입니다."),
	BOOKMARK_NOT_FOUND(HttpStatus.NOT_FOUND, "BM002", "즐겨찾기를 찾을 수 없습니다."),

	//User
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "해당 사용자를 찾을 수 없습니다."),

	//Review
	REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "R001", "해당 후기를 찾을 수 없습니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;

	ErrorCode(HttpStatus status, String code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}
}