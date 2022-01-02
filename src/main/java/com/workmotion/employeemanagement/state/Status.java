package com.workmotion.employeemanagement.state;

public enum Status {
	ADDED(1, false),
	IN_CHECK(2, true),
	APPROVED(3, false),
	ACTIVE(4, false);

	private final int index;
	private final boolean extended;

	Status(int index, boolean extended) {
		this.index = index;
		this.extended = extended;
	}

	int getIndex() {
		return this.index;
	}

	boolean isExtended() {
		return this.extended;
	}
}
