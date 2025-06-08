package com.example.bloombackend.item.entity.items;

import com.example.bloombackend.item.entity.ItemEntity;
import com.example.bloombackend.item.entity.ItemType;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("SEED")
@SuperBuilder
public class SeedEntity extends ItemEntity {
	@Column(name = "small_icon_url", nullable = false)
	private String smallIconUrl;

	@Getter
	@Column(name = "big_icon_url", nullable = false)
	private String bigIconUrl;

	public String getType() {
		return ItemType.SEED.toString();
	}
}
