package com.stoov.user.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import com.stoov.common.entity.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity
{
	@Id
	@Column(name = "user_id", nullable = false, updatable = false)
	private UUID id;

	@Column(name = "email", nullable = false)
	private String email;

	//소셜 로그인의 고유 식별자
	@Column(name = "sub", nullable = false, length = 64)
	private String sub;

	@Column(name = "nickname", nullable = false)
	private String nickname;

	@Column(name = "profile_image_url")
	private String profileImageUrl;
}
