package com.stoov.user.controller;

import java.util.UUID;

import com.stoov.common.response.CustomApiResponse;
import com.stoov.user.dto.GoogleOAuthRequest;
import com.stoov.user.dto.LoginResponse;
import com.stoov.user.service.GoogleOAuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    public static final String USER_ID = "userId";
	private final GoogleOAuthService googleOAuthService;

	@PostMapping("/google")
	public ResponseEntity<CustomApiResponse<LoginResponse>> googleOAuth(
		@RequestBody GoogleOAuthRequest request,
		HttpServletRequest httpServletRequest
	) {
        // credential 검증 및 유저 조회/생성
		LoginResponse response = googleOAuthService.login(request);

        // 세션에만 userId 저장
		setSession(httpServletRequest, response);

		return ResponseEntity.ok(CustomApiResponse.success(response));
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
			throw new IllegalStateException("LoginResponse에 userId가 비어 있음");
		}
		return userId;
	}
}
