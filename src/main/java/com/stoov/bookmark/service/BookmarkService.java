package com.stoov.bookmark.service;

import com.stoov.bookmark.entity.Bookmark;
import com.stoov.bookmark.repository.BookmarkRepository;
import com.stoov.common.exception.BusinessException;
import com.stoov.common.exception.ErrorCode;
import com.stoov.place.entity.Place;
import com.stoov.place.repository.PlaceRepository;
import com.stoov.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final PlaceRepository placeRepository;

    @Transactional
    public void addBookmark(Long placeId, User user) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PLACE_NOT_FOUND));

        bookmarkRepository.findByUserAndPlace(user, place).ifPresent(bookmark -> {
            throw new BusinessException(ErrorCode.ALREADY_BOOKMARKED);
        });

        Bookmark bookmark = Bookmark.builder()
                .user(user)
                .place(place)
                .build();
        bookmarkRepository.save(bookmark);
    }

    @Transactional
    public void deleteBookmark(Long placeId, User user) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PLACE_NOT_FOUND));

        Bookmark bookmark = bookmarkRepository.findByUserAndPlace(user, place)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOOKMARK_NOT_FOUND));

        bookmarkRepository.delete(bookmark);
    }
}
