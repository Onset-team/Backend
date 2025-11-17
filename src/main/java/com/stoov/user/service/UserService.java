package com.stoov.user.service;

import com.stoov.user.dto.MyPageResponse;

import java.util.UUID;

public interface UserService {
    public MyPageResponse getMyPage(UUID userId);
}
