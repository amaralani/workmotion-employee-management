package com.workmotion.employeemanagement.state;

public enum SecurityCheckStatus {
	SECURITY_CHECK_STARTED(1),
	SECURITY_CHECK_FINISHED(2);

	private final int index;

	SecurityCheckStatus(int index) {
		this.index = index;
	}

	public int getIndex() {
		return this.index;
	}

	public boolean canChangeTo(SecurityCheckStatus state) {
		return state.getIndex() - this.getIndex() == 0 || state.getIndex() - this.getIndex() == 1;
	}
}