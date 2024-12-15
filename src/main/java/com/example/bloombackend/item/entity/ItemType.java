package com.example.bloombackend.item.entity;

public enum ItemType {
	SEED("씨앗"), SEASON("시즌"), BUTTON("단추"), DECO("장식");

	private final String label;

	ItemType(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
