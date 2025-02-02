package com.example.bloombackend.item.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "item_type")
@Table(name = "item")
@SuperBuilder
public abstract class ItemEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "item_id")
	@Getter
	private Long id;

	@Column(name = "name", nullable = false)
	@Getter
	private String name;

	@Column(name = "price", nullable = false)
	@Getter
	private Integer price;

	@Column(name = "thumbnail", nullable = false)
	@Getter
	private String thumbnailImgUrl;

	@Column(name = "is_default")
	private Boolean isDefault;

	@Column(name = "end_date")
	@Getter
	private LocalDate endDate;

	public abstract String getType();
}
