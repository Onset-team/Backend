package com.stoov.bookmark.repository;

import com.stoov.bookmark.entity.Bookmark;
import com.stoov.place.entity.Place;
import com.stoov.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    Optional<Bookmark> findByUserAndPlace(User user, Place place);

    void deleteByUserAndPlace(User user, Place place);

    List<Bookmark> findAllByUser(User user);
}
