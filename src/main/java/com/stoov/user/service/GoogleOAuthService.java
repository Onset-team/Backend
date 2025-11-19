package com.stoov.user.service;

import com.stoov.user.dto.GoogleOAuthRequest;
import com.stoov.user.dto.LoginResponse;

public interface GoogleOAuthService {
    public LoginResponse login(GoogleOAuthRequest request);
}
