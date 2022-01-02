package com.workmotion.employeemanagement;

import com.workmotion.employeemanagement.domain.Employee;
import com.workmotion.employeemanagement.dto.EmployeeResponse;
import com.workmotion.employeemanagement.state.EmployeeStateProvider;
import com.workmotion.employeemanagement.state.Status;
import java.util.UUID;

public final class TestUtil {
	private TestUtil() {
	}

	public static Employee createEmployee(int index, Status status) {
		return Employee.builder().id(UUID.randomUUID().toString()).fullName("name " + index).username("username " + index).employeeState(EmployeeStateProvider.getByStatus(status)).build();
	}

	public static boolean matches(EmployeeResponse employeeResponse, Employee employee) {
		return employeeResponse.getId().equals(employee.getId()) && employeeResponse.getUsername().equals(employee.getUsername()) && employeeResponse.getName().equals(employee.getFullName()) && employeeResponse.getApplicationState().equals(employee.getEmployeeState().getStatus().name());
	}
}