package com.stoov.bookmark.controller;

import com.stoov.bookmark.dto.BookmarkResponse;
import com.stoov.bookmark.service.BookmarkService;
import com.stoov.common.response.CustomApiResponse;
import com.stoov.user.helper.UserResolver;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final UserResolver userResolver;

    /**
     * 관심장소 목록 조회
     * @param request
     * @return
     */
    @GetMapping
    public ResponseEntity<CustomApiResponse<List<BookmarkResponse>>> getBookmark(HttpServletRequest request) {
        UUID userId = userResolver.resolveUserId(request);
        return ResponseEntity.ok(CustomApiResponse.success(bookmarkService.getBookmark(userId)));
    }
}
