package com.stoov.user.entity;

import java.util.UUID;

import com.stoov.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {
    @Id
    @Column(name = "user_id", nullable = false)
    private UUID id;

    @Column(name = "email", nullable = false, length = 254)
    private String email;

    //소셜 로그인의 고유 식별자
    @Column(name = "sub", nullable = false, length = 64)
    private String sub;

    @Column(name = "nickname", nullable = false, length = 30)
    private String nickname;

    @Column(name = "profile_image_url")
    private String profileImageUrl;


    public static User createUser(String email, String sub, String nickname, String profileImageUrl) {
        User user = new User();
        user.id = UUID.randomUUID();
        user.email = email;
        user.sub = sub;
        user.nickname = nickname;
        user.profileImageUrl = profileImageUrl;
        return user;
    }

    public String getMaskedNickname() {
        return nickname.substring(0, 6) + "***";
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
