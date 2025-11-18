package com.stoov.user.service;

import com.stoov.user.dto.MyPageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface UserService {

	MyPageResponse getMyPage(UUID userId);

	MyPageResponse updateMyProfileImage(UUID userId, MultipartFile file);

	void deleteUser(UUID userId);

	void saveUserAgreement(UUID userId);
}
