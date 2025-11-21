package com.stoov.user.helper;

import com.stoov.common.exception.BusinessException;
import com.stoov.common.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserResolver {

    public static final String USER_ID = "userId";

    /**
     * 로그인 여부가 선택적인 경우 사용.
     * 세션이 없거나 userId가 없으면 null 반환.
     */
    public UUID resolveUserIdOrNull(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession(false);
        if (session == null) {
            return null;
        }

        Object userId = session.getAttribute(USER_ID);
        if (userId instanceof UUID) {
            return (UUID) userId;
        }

        return null;
    }

    /**
     * 반드시 로그인되어 있어야 하는 경우 사용.
     * userId가 없으면 UNAUTHORIZED 에러를 발생시킨다.
     */
    public UUID resolveRequiredUserId(HttpServletRequest httpServletRequest) {
        UUID userId = resolveUserIdOrNull(httpServletRequest);
        if (userId == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        return userId;
    }
}
