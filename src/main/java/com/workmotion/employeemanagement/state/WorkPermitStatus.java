package com.workmotion.employeemanagement.state;

public enum WorkPermitStatus {
	WORK_PERMIT_CHECK_STARTED(1),
	WORK_PERMIT_CHECK_FINISHED(2);

	private final int index;

	WorkPermitStatus(int index) {
		this.index = index;
	}

	public int getIndex() {
		return this.index;
	}

	public boolean canChangeTo(WorkPermitStatus state) {
		return state.getIndex() - this.getIndex() == 0 || state.getIndex() - this.getIndex() == 1;
	}
}