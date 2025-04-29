package com.example.bloombackend.bottlemsg.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.example.bloombackend.bottlemsg.controller.dto.response.Info.BottleMessageInfo;
import com.example.bloombackend.bottlemsg.controller.dto.response.Info.BottleMessageSummaryInfo;
import com.example.bloombackend.user.entity.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "bottle_message")
public class BottleMessageEntity {
	@Getter
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "message_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private UserEntity sender;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "content", length = 500, nullable = false)
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "postcard_id")
	private PostcardEntity postcard;

	@Enumerated(EnumType.STRING)
	@Column(name = "negativity")
	private Negativity negativity;

	@Getter
	@CreationTimestamp
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Builder
	public BottleMessageEntity(UserEntity user, String content, String title, PostcardEntity postcard,
		Negativity negativity) {
		this.sender = user;
		this.content = content;
		this.title = title;
		this.postcard = postcard;
		this.negativity = negativity;
	}

	public BottleMessageInfo toDetailInfo() {
		return BottleMessageInfo.builder()
			.messageId(id)
			.content(content)
			.title(title)
			.postCardUrl(postcard.getPostcardUrl())
			.negativity(negativity.name())
			.build();
	}

	public BottleMessageSummaryInfo toSummaryInfo() {
		return BottleMessageSummaryInfo.builder()
			.messageId(id)
			.title(title)
			.postCardUrl(postcard.getPostcardUrl())
			.negativity(negativity.name())
			.build();
	}
}
