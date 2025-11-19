package com.stoov.user.service;

import com.stoov.common.exception.BusinessException;
import com.stoov.common.exception.ErrorCode;
import com.stoov.user.dto.GoogleOAuthRequest;
import com.stoov.user.dto.LoginResponse;
import com.stoov.user.entity.User;
import com.stoov.user.google.GoogleCredentialPayload;
import com.stoov.user.google.GoogleCredentialVerifier;
import com.stoov.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GoogleOAuthServiceImpl implements GoogleOAuthService {

    private final GoogleCredentialVerifier googleCredentialVerifier;
    private final UserRepository userRepository;

    public LoginResponse login(GoogleOAuthRequest request) {

        GoogleCredentialPayload payload = googleCredentialVerifier.verify(request);
        Optional<User> optionalUser = userRepository.findBySub(payload.getSub());

        boolean isNewUser;
        User user;

        if(optionalUser.isPresent()){
            // 기존 유저 (단순 로그인)
            isNewUser = false;
            user = optionalUser.get();
        } else {
            // 신규 유저 (회원가입 + 로그인)
            isNewUser = true;
            user = userRepository.save(
                    User.createUser(
                            payload.getEmail(),
                            payload.getSub(),
                            generateNicknameFromEmail(payload.getEmail()),
                            payload.getProfileImageUrl()
                    )
            );
        }

        return LoginResponse.from(user, isNewUser);
    }

    // 이메일의 '@' 앞부분까지를 닉네임으로 반환
    private String generateNicknameFromEmail(String email){
        if (email == null) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "payload의 이메일이 비어 있음");
        }

        int atIndex = email.indexOf("@");

        if (atIndex <= 0) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "이메일 형식이 아님: " + email);
        }
        return email.substring(0, atIndex);
    }
}
