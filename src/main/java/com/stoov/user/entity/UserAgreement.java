package com.stoov.user.entity;

import com.stoov.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "user_agreement",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_user_agreement_user",
                        columnNames = "user_id"
                )
        },
        indexes = {
                @Index(
                        name = "idx_user_agreement_email",
                        columnList = "user_email"
                ),
                @Index(
                        name = "idx_user_agreement_nickname",
                        columnList = "user_nickname"
                )
        }
)
@Getter
@NoArgsConstructor
public class UserAgreement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_agreement_id", nullable = false, updatable = false)
    private Long id;

    // FK를 걸지 않고 UUID만 저장
    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID userId;

    @Column(name = "user_email", nullable = false, length = 254)
    private String userEmail;

    @Column(name = "user_nickname", nullable = false, length = 30)
    private String userNickname;

    @Column(name = "agreed_at", nullable = false)
    private OffsetDateTime agreedAt;

    @Column(name = "user_deleted_at")
    private OffsetDateTime userDeletedAt;

    public static UserAgreement create(UUID userId, String email, String nickname, OffsetDateTime now) {
        UserAgreement agreement = new UserAgreement();
        agreement.userId = userId;
        agreement.userEmail = email;
        agreement.userNickname = nickname;
        agreement.agreedAt = now;
        return agreement;
    }

    public void markUserDeleted(OffsetDateTime deletedAt) {
        this.userDeletedAt = deletedAt;
    }
}