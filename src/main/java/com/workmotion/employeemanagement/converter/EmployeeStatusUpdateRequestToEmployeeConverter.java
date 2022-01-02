package com.workmotion.employeemanagement.converter;

import com.workmotion.employeemanagement.domain.Employee;
import com.workmotion.employeemanagement.dto.EmployeeStatusUpdateRequest;
import com.workmotion.employeemanagement.service.EmployeeService;
import com.workmotion.employeemanagement.service.InvalidEmployeeIdException;
import com.workmotion.employeemanagement.state.EmployeeState;
import com.workmotion.employeemanagement.state.EmployeeStateProvider;
import com.workmotion.employeemanagement.state.ExtendedEmployeeState;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class EmployeeStatusUpdateRequestToEmployeeConverter implements Converter<EmployeeStatusUpdateRequest,
		Employee> {
	private final EmployeeService employeeService;

	public EmployeeStatusUpdateRequestToEmployeeConverter(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public Employee convert(EmployeeStatusUpdateRequest source) {
		Employee currentEmployee = this.employeeService.getById(source.getId());
		if (Objects.isNull(currentEmployee)) {
			throw new InvalidEmployeeIdException("Invalid employee id");
		} else {
			EmployeeState newState = EmployeeStateProvider.getByStatus(source.getStatus());
			if (newState instanceof ExtendedEmployeeState) {
				ExtendedEmployeeState employeeState = (ExtendedEmployeeState) newState;
				EmployeeState oldEmployeeState = currentEmployee.getEmployeeState();
				this.setSecurityCheckStatus(source, employeeState, oldEmployeeState);
				this.setWorkPermitStatus(source, employeeState, oldEmployeeState);
			}

			return Employee.builder().id(currentEmployee.getId()).username(currentEmployee.getUsername()).fullName(currentEmployee.getFullName()).employeeState(newState).build();
		}
	}

	private void setWorkPermitStatus(EmployeeStatusUpdateRequest source, ExtendedEmployeeState employeeState,
									 EmployeeState oldEmployeeState) {
		if (Objects.nonNull(source.getWorkPermitStatus())) {
			employeeState.setWorkPermitStatus(source.getWorkPermitStatus());
		} else if (oldEmployeeState instanceof ExtendedEmployeeState) {
			employeeState.setWorkPermitStatus(((ExtendedEmployeeState) oldEmployeeState).getWorkPermitStatus());
		}

	}

	private void setSecurityCheckStatus(EmployeeStatusUpdateRequest source, ExtendedEmployeeState employeeState,
										EmployeeState oldEmployeeState) {
		if (Objects.nonNull(source.getSecurityCheckStatus())) {
			employeeState.setSecurityCheckStatus(source.getSecurityCheckStatus());
		} else if (oldEmployeeState instanceof ExtendedEmployeeState) {
			employeeState.setSecurityCheckStatus(((ExtendedEmployeeState) oldEmployeeState).getSecurityCheckStatus());
		}

	}
}