package com.stoov.user.repository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import com.stoov.user.entity.UserAgreement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAgreementRepository extends JpaRepository<UserAgreement, Long> {

	boolean existsByUserIdAndUserDeletedAtIsNull(UUID userId);

	Optional<UserAgreement> findByUserId(UUID userId);

	void deleteByUserDeletedAtBefore(OffsetDateTime threshold);
}
