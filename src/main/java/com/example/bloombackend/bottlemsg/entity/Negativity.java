package com.example.bloombackend.bottlemsg.entity;

public enum Negativity {
	UPPER("상"), MIDDLE("중"), LOWER("하"), UNKNOWN("분석실패");

	private final String label;

	Negativity(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
