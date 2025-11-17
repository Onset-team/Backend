package com.stoov.user.service;

import com.stoov.common.exception.BusinessException;
import com.stoov.common.exception.ErrorCode;
import com.stoov.user.dto.MyPageResponse;
import com.stoov.user.entity.User;
import com.stoov.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final S3Client s3Client;

    @Value("${s3.bucket}")
    private String bucket;

    @Value("${s3.base-url:}")
    private String baseUrl;

    public MyPageResponse getMyPage(UUID userId) {
        if (userId == null) {
            return MyPageResponse.builder()
                    .nickname(null)
                    .profileImageUrl(null)
                    .build();
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        return MyPageResponse.from(user.getNickname(), user.getProfileImageUrl());
    }


    @Value("${s3.enabled:true}")
    private boolean s3Enabled;

    //프로필 수정. 형태만 일단 갖추고, 제대로 된 업데이트는 MVP 이후에
    @Transactional
    public MyPageResponse updateMyProfileImage(UUID userId, MultipartFile file) {
        if (userId == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        String url = uploadProfileImage(userId, file);
        user.setProfileImageUrl(url);

        return MyPageResponse.from(user.getNickname(), user.getProfileImageUrl());
    }

    //프로필 이미지 업로드용 내부 메서드, 추후 이미지 업로드를 확장한다면 별도 클래스로 분리
    private String uploadProfileImage(UUID userId, MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String extension = StringUtils.getFilenameExtension(originalFilename);
        if (!StringUtils.hasText(extension)) {
            extension = "png";
        }

        String key = "users/" + userId + "/profile-" + System.currentTimeMillis() + "." + extension;

        // 로컬 테스트용: S3 호출 건너뛰기(실제 이미지 저장x)
        if (!s3Enabled){
            return buildUrlFromBase(key);
        }

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(file.getContentType())
                .build();

        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "프로필 이미지를 업로드하지 못했습니다.");
        }

        if (!StringUtils.hasText(baseUrl)) {
            return key;
        }

        if (baseUrl.endsWith("/")) {
            return baseUrl + key;
        }

        return baseUrl + "/" + key;
    }
    private String buildUrlFromBase(String key) {
        if (!StringUtils.hasText(baseUrl)) {
            return key;
        }
        return baseUrl.endsWith("/") ? baseUrl + key : baseUrl + "/" + key;
    }
}
