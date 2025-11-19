package com.stoov.user.google;

import lombok.*;

@Getter
@Builder
public class GoogleCredentialPayload {
    private String sub;
    private String email;
    private String profileImageUrl;
}
