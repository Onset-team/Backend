package com.stoov.bookmark.service;

import com.stoov.bookmark.dto.BookmarkResponse;
import com.stoov.bookmark.entity.Bookmark;
import com.stoov.bookmark.repository.BookmarkRepository;
import com.stoov.common.exception.BusinessException;
import com.stoov.common.exception.ErrorCode;
import com.stoov.place.entity.Place;
import com.stoov.place.repository.PlaceRepository;
import com.stoov.review.repository.ReviewRepository;
import com.stoov.user.entity.User;
import com.stoov.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    /**
     * 관심장소 등록
     */
    @Transactional
    public void addBookmark(Long placeId, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
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

    /**
     * 관심장소 해제
     */
    @Transactional
    public void deleteBookmark(Long placeId, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PLACE_NOT_FOUND));

        Bookmark bookmark = bookmarkRepository.findByUserAndPlace(user, place)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOOKMARK_NOT_FOUND));

        bookmarkRepository.delete(bookmark);
    }

    /**
     * 관심장소 목록 조회
     */
    @Transactional(readOnly = true)
    public List<BookmarkResponse> getBookmark(UUID userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        List<Bookmark> bookmarks = bookmarkRepository.findAllByUser(user);

        return bookmarks.stream()
                .map(bookmark -> {
                    Place place = bookmark.getPlace();
                    long reviewCount = reviewRepository.countByPlace(place);
                    return BookmarkResponse.of(place, reviewCount);
                })
                .collect(Collectors.toList());
    }
}
