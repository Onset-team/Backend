package com.stoov.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GoogleOAuthRequest {

    private String credential;
    private String clientId;
    private String select_by;
}
