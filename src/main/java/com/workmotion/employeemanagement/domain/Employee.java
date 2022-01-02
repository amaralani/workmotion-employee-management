package com.workmotion.employeemanagement.domain;

import com.workmotion.employeemanagement.state.EmployeeState;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Employee {
	private String id;
	private String username;
	private String fullName;
	private EmployeeState employeeState;
}
