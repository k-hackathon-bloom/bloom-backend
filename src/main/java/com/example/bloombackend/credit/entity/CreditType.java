package com.example.bloombackend.credit.entity;

public enum CreditType {
	BUTTON("단추"), CASH("현금");

	private final String label;

	CreditType(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
