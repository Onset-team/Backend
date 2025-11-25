package com.stoov.review.repository;

import com.stoov.place.entity.Place;
import com.stoov.review.entity.Review;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    long countByPlace(Place place);

    //리뷰 목록 조회 시 user까지 같이 읽어오도록
    @EntityGraph(attributePaths = "user")
    List<Review> findAllByPlaceOrderByCreatedAtDesc(Place place);

    //여러 장소에 대한 리뷰 개수 한번에 조회
    @Query("""
        select r.place.id, count(r)
        from Review r
        where r.place in :places
        group by r.place.id
        """)
    List<Object[]> countByPlaces(@Param("places") List<Place> places);
}
