package com.stoov.review.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
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
@Table(name = "review")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review extends BaseEntity
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "review_id", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_review_user"))
	private User user;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "place_id", nullable = false, foreignKey = @ForeignKey(name = "fk_review_place"))
	private Place place;

	//후기 내용
	@Lob
	@Column(nullable = false)
	private String content;

}
