package com.stoov.review.repository;

import com.stoov.place.entity.Place;
import com.stoov.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    long countByPlace(Place place);
    List<Review> findAllByPlaceOrderByCreatedAtDesc(Place place);
}
