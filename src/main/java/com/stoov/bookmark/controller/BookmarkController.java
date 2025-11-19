package com.stoov.bookmark.controller;

import com.stoov.bookmark.service.BookmarkService;
import com.stoov.common.exception.BusinessException;
import com.stoov.common.exception.ErrorCode;
import com.stoov.common.response.CustomApiResponse;
import com.stoov.user.helper.UserResolver;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/places/{placeId}/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final UserResolver userResolver;

    @PostMapping
    public ResponseEntity<CustomApiResponse<?>> addBookmark(
            @PathVariable Long placeId,
            HttpServletRequest request) {
        UUID userId = userResolver.resolveUserId(request);
        if (userId == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        bookmarkService.addBookmark(placeId, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(CustomApiResponse.success());
    }

    @DeleteMapping
    public ResponseEntity<CustomApiResponse<?>> deleteBookmark(
            @PathVariable Long placeId,
            HttpServletRequest request) {
        UUID userId = userResolver.resolveUserId(request);
        if (userId == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        bookmarkService.deleteBookmark(placeId, userId);
        return ResponseEntity.ok(CustomApiResponse.success());
    }
}
