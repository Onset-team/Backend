package com.stoov.user.controller;

import java.util.UUID;

import com.stoov.common.exception.BusinessException;
import com.stoov.common.exception.ErrorCode;
import com.stoov.common.response.CustomApiResponse;
import com.stoov.user.dto.GoogleOAuthRequest;
import com.stoov.user.dto.LoginResponse;
import com.stoov.user.dto.MyPageResponse;
import com.stoov.user.helper.UserResolver;
import com.stoov.user.service.GoogleOAuthService;

import com.stoov.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.stoov.user.helper.UserResolver.USER_ID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {


	private final GoogleOAuthService googleOAuthService;
    private final UserService userService;
    private final UserResolver userResolver;

	@PostMapping("/google")
	public ResponseEntity<CustomApiResponse<LoginResponse>> googleOAuth(
		@RequestBody GoogleOAuthRequest request,
		HttpServletRequest httpServletRequest
	) {
        // credential 검증 및 유저 조회/생성
		LoginResponse response = googleOAuthService.login(request);

        // 세션에 userId 저장
		setSession(httpServletRequest, response);

		return ResponseEntity.ok(CustomApiResponse.success(response));
	}

    @PostMapping("/agreements")
    public ResponseEntity<CustomApiResponse<?>> saveUserAgreement(HttpServletRequest httpServletRequest) {
        UUID userId = userResolver.resolveRequiredUserId(httpServletRequest);
        userService.saveUserAgreement(userId);
        return ResponseEntity.ok(CustomApiResponse.success());
    }

    @GetMapping("/my")
    public ResponseEntity<CustomApiResponse<MyPageResponse>> getMyPage(
            HttpServletRequest httpServletRequest
    ){
        UUID userId = userResolver.resolveUserIdOrNull(httpServletRequest);
        MyPageResponse response = userService.getMyPage(userId);

        return ResponseEntity.ok(CustomApiResponse.success(response));
    }

    @PutMapping(value = "/my", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CustomApiResponse<MyPageResponse>> updateMyProfileImage(
            HttpServletRequest request,
            @RequestParam("file") MultipartFile file
    ) {
        UUID userId = userResolver.resolveRequiredUserId(request);
        MyPageResponse response = userService.updateMyProfileImage(userId, file);

        return ResponseEntity.ok(CustomApiResponse.success(response));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(HttpServletRequest httpServletRequest) {
        UUID userId = userResolver.resolveRequiredUserId(httpServletRequest);
        userService.deleteUser(userId);

        return ResponseEntity.noContent().build();
    }

	private void setSession(HttpServletRequest httpServletRequest, LoginResponse response) {
        // 세션이 없으면 새로 생성
		HttpSession session = httpServletRequest.getSession(true);

        // 세션에 userId 저장
		session.setAttribute(USER_ID, requireUserId(response));
	}

	private UUID requireUserId(LoginResponse response) {
		UUID userId = response.getUserId();
		if (userId == null) {
			throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "LoginResponse에 userId가 비어 있음");
		}
		return userId;
	}
}
