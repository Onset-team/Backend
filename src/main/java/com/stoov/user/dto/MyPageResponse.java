package com.stoov.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyPageResponse {
    private String nickname;
    private String profileImageUrl;

    public static MyPageResponse from(String nickname, String profileImageUrl) {
        return MyPageResponse.builder()
                .nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .build();
    }
}
