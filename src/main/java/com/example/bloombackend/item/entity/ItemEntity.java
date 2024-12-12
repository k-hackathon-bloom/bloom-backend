package com.example.bloombackend.item.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "item")
@Getter
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

	@Builder
	public ItemEntity(ItemType type, String name, Integer price, String imgUrl, Boolean isSale) {
		this.type = type;
		this.name = name;
		this.price = price;
		this.imgUrl = imgUrl;
		this.isSale = isSale;
	}
}
