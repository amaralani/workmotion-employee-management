package com.workmotion.employeemanagement.state;

/**
 * A factory-like class to provide state based on status.
 */
public class EmployeeStateProvider {
	public EmployeeStateProvider() {
	}

	public static EmployeeState getByStatus(Status state) {
		return state.isExtended() ? new ExtendedEmployeeState(state) : new EmployeeState(state);
	}
}