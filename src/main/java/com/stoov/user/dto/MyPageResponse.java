package com.stoov.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyPageResponse {
    private String email;
    private String nickname;
    private String profileImageUrl;

    public static MyPageResponse from(String email, String nickname, String profileImageUrl) {
        return MyPageResponse.builder()
                .email(email)
                .nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .build();
    }
}
