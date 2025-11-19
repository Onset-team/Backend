package com.stoov.bookmark.controller;

import com.stoov.bookmark.service.BookmarkService;
import com.stoov.common.response.CustomApiResponse;
import com.stoov.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/places/{placeId}/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping
    public ResponseEntity<CustomApiResponse<?>> addBookmark(
            @PathVariable Long placeId,
            @AuthenticationPrincipal User user) {
        // Assuming User object is directly available from the principal
        // If not, this needs to be adjusted based on the actual UserDetails implementation
        bookmarkService.addBookmark(placeId, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(CustomApiResponse.success());
    }

    @DeleteMapping
    public ResponseEntity<CustomApiResponse<?>> deleteBookmark(
            @PathVariable Long placeId,
            @AuthenticationPrincipal User user) {
        // Assuming User object is directly available from the principal
        bookmarkService.deleteBookmark(placeId, user);
        return ResponseEntity.ok(CustomApiResponse.success());
    }
}
