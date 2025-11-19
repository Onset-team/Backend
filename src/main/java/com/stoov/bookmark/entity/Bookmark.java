package com.stoov.bookmark.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.stoov.common.entity.BaseEntity;
import com.stoov.place.entity.Place;
import com.stoov.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bookmark")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bookmark extends BaseEntity
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "favorite_id", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_favorite_user"))
	private User user;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "place_id", nullable = false, foreignKey = @ForeignKey(name = "fk_favorite_place"))
	private Place place;

}
