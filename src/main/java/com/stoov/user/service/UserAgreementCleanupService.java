package com.stoov.user.service;

import java.time.OffsetDateTime;

import com.stoov.user.repository.UserAgreementRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAgreementCleanupService {

	private final UserAgreementRepository userAgreementRepository;

	// 매일 새벽 3시에, 탈퇴 후 3년이 지난 약관 동의 이력 삭제
	@Scheduled(cron = "0 0 3 * * *")
	@Transactional
	public void purgeOldAgreements() {
		OffsetDateTime threshold = OffsetDateTime.now().minusYears(3);
		userAgreementRepository.deleteByUserDeletedAtBefore(threshold);
	}
}

