package com.stoov.user.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.stoov.user.entity.User;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
	private boolean isNewUser;
	private String nickname;
	private String profileImageUrl;
	@JsonIgnore
	private UUID userId;

	public static LoginResponse from(User user, boolean isNewUser) {
		return LoginResponse.builder()
			.isNewUser(isNewUser)
			.nickname(user.getNickname())
			.profileImageUrl(user.getProfileImageUrl())
			.userId(user.getId())
			.build();
	}
}
