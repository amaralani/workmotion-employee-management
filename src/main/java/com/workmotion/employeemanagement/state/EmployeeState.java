package com.workmotion.employeemanagement.state;

import java.util.Objects;

/**
 * Handles basic states of an employee.
 */
public class EmployeeState {
	private final Status status;

	protected EmployeeState(Status status) {
		this.status = status;
	}

	public Status getStatus() {
		return this.status;
	}

	public boolean canChangeTo(EmployeeState employeeState) {
		// Self change is supported
		return employeeState.getStatus().getIndex() - this.getStatus().getIndex() == 0
				|| employeeState.getStatus().getIndex() - this.getStatus().getIndex() == 1;
	}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (o != null && this.getClass() == o.getClass()) {
			EmployeeState employeeState = (EmployeeState) o;
			return this.getStatus().getIndex() == employeeState.getStatus().getIndex();
		} else {
			return false;
		}
	}

	public int hashCode() {
		return Objects.hash(this.status);
	}
}
