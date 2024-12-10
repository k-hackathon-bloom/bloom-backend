package com.example.bloombackend.store.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "item")
public class ItemEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "item_id")
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false)
	private ItemType type;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "price", nullable = false)
	private Integer price;

	@Column(name = "img_url", nullable = false)
	private String imgUrl;

	@Column(name = "is_sale")
	private Boolean isSale;
}
