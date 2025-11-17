package com.stoov.user.service;

import com.stoov.user.dto.MyPageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface UserService {
    public MyPageResponse getMyPage(UUID userId);
    public MyPageResponse updateMyProfileImage(UUID userId, MultipartFile file);
}
