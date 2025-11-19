package com.stoov.place.repository;

import com.stoov.place.entity.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    @Query("SELECT p FROM Place p WHERE REPLACE(p.name, ' ', '') LIKE %:keyword% OR REPLACE(p.district, ' ', '') LIKE %:keyword%")
    Page<Place> searchByNameOrDistrict(@Param("keyword") String keyword, Pageable pageable);
}
