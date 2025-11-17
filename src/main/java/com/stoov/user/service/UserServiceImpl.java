package com.stoov.user.service;

import com.stoov.common.exception.BusinessException;
import com.stoov.common.exception.ErrorCode;
import com.stoov.user.dto.MyPageResponse;
import com.stoov.user.entity.User;
import com.stoov.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public MyPageResponse getMyPage(UUID userId) {
        if (userId == null) {
            return MyPageResponse.builder()
                    .nickname(null)
                    .profileImageUrl(null)
                    .build();
        }

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return MyPageResponse.builder()
                    .nickname(user.getNickname())
                    .profileImageUrl(user.getProfileImageUrl())
                    .build();
        } else {
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }
    }
}
