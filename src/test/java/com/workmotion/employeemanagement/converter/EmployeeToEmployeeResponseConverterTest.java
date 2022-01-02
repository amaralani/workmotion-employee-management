package com.workmotion.employeemanagement.converter;

import com.workmotion.employeemanagement.TestUtil;
import com.workmotion.employeemanagement.domain.Employee;
import com.workmotion.employeemanagement.dto.EmployeeResponse;
import com.workmotion.employeemanagement.state.ExtendedEmployeeState;
import com.workmotion.employeemanagement.state.SecurityCheckStatus;
import com.workmotion.employeemanagement.state.Status;
import com.workmotion.employeemanagement.state.WorkPermitStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EmployeeToEmployeeResponseConverterTest {
	EmployeeToEmployeeResponseConverter converter;

	public EmployeeToEmployeeResponseConverterTest() {
	}

	@BeforeEach
	public void beforeEach() {
		converter = new EmployeeToEmployeeResponseConverter();
	}

	@Test
	public void convertEmployeeWithEmployeeStateShouldNotHaveExtendedProperties() {
		Employee employee = TestUtil.createEmployee(1, Status.ADDED);

		EmployeeResponse employeeResponse = converter.convert(employee);

		Assertions.assertThat(employeeResponse).isNotNull();
		Assertions.assertThat(employeeResponse.getName()).isEqualTo(employee.getFullName());
		Assertions.assertThat(employeeResponse.getUsername()).isEqualTo(employee.getUsername());
		Assertions.assertThat(employeeResponse.getApplicationState()).isEqualTo(employee.getEmployeeState().getStatus().name());
		Assertions.assertThat(employeeResponse.getSecurityCheckStatus()).isNull();
		Assertions.assertThat(employeeResponse.getWorkPermitStatus()).isNull();
	}

	@Test
	public void convertEmployeeWithExtendedEmployeeStateShouldHaveExtendedProperties() {
		Employee employee = TestUtil.createEmployee(1, Status.IN_CHECK);
		ExtendedEmployeeState employeeState = (ExtendedEmployeeState)employee.getEmployeeState();
		employeeState.setWorkPermitStatus(WorkPermitStatus.WORK_PERMIT_CHECK_STARTED);
		employeeState.setSecurityCheckStatus(SecurityCheckStatus.SECURITY_CHECK_FINISHED);

		EmployeeResponse employeeResponse = converter.convert(employee);

		Assertions.assertThat(employeeResponse).isNotNull();
		Assertions.assertThat(employeeResponse.getName()).isEqualTo(employee.getFullName());
		Assertions.assertThat(employeeResponse.getUsername()).isEqualTo(employee.getUsername());
		Assertions.assertThat(employeeResponse.getApplicationState()).isEqualTo(employee.getEmployeeState().getStatus().name());
		Assertions.assertThat(employeeResponse.getSecurityCheckStatus()).isEqualTo(SecurityCheckStatus.SECURITY_CHECK_FINISHED.name());
		Assertions.assertThat(employeeResponse.getWorkPermitStatus()).isEqualTo(WorkPermitStatus.WORK_PERMIT_CHECK_STARTED.name());
	}
}
