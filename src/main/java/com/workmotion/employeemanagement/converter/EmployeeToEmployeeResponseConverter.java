package com.workmotion.employeemanagement.converter;

import com.workmotion.employeemanagement.domain.Employee;
import com.workmotion.employeemanagement.dto.EmployeeResponse;
import com.workmotion.employeemanagement.state.ExtendedEmployeeState;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EmployeeToEmployeeResponseConverter implements Converter<Employee, EmployeeResponse> {
	public EmployeeToEmployeeResponseConverter() {
	}

	public EmployeeResponse convert(Employee source) {
		EmployeeResponse response = new EmployeeResponse();
		response.setId(source.getId());
		response.setName(source.getFullName());
		response.setUsername(source.getUsername());
		response.setApplicationState(source.getEmployeeState().getStatus().name());
		if (source.getEmployeeState() instanceof ExtendedEmployeeState) {
			ExtendedEmployeeState employeeState = (ExtendedEmployeeState) source.getEmployeeState();
			response.setSecurityCheckStatus(employeeState.getSecurityCheckStatus().name());
			response.setWorkPermitStatus(employeeState.getWorkPermitStatus().name());
		}

		return response;
	}
}
