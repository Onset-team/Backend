package com.stoov.user.helper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserResolver {

    public static final String USER_ID = "userId";
    public UUID resolveUserId(HttpServletRequest httpServletRequest) {
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
}
