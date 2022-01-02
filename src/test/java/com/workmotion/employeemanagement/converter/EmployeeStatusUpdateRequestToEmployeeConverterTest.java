package com.workmotion.employeemanagement.converter;

import com.workmotion.employeemanagement.TestUtil;
import com.workmotion.employeemanagement.domain.Employee;
import com.workmotion.employeemanagement.dto.EmployeeStatusUpdateRequest;
import com.workmotion.employeemanagement.service.EmployeeService;
import com.workmotion.employeemanagement.service.InvalidEmployeeIdException;
import com.workmotion.employeemanagement.state.ExtendedEmployeeState;
import com.workmotion.employeemanagement.state.SecurityCheckStatus;
import com.workmotion.employeemanagement.state.Status;
import com.workmotion.employeemanagement.state.WorkPermitStatus;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class EmployeeStatusUpdateRequestToEmployeeConverterTest {
	private EmployeeStatusUpdateRequestToEmployeeConverter converter;
	private EmployeeService mockEmployeeService;

	public EmployeeStatusUpdateRequestToEmployeeConverterTest() {
	}

	@BeforeEach
	public void beforeEach() {
		mockEmployeeService = Mockito.mock(EmployeeService.class);
		this.converter = new EmployeeStatusUpdateRequestToEmployeeConverter(mockEmployeeService);
	}

	@Test
	public void employeeDoesNotExistShouldThrowException() {
		String id = UUID.randomUUID().toString();
		EmployeeStatusUpdateRequest request = new EmployeeStatusUpdateRequest();
		request.setId(id);

		Mockito.doReturn(null).when(mockEmployeeService).getById(id);

		Assertions.assertThrows(InvalidEmployeeIdException.class, () -> {
			this.converter.convert(request);
		});

		Mockito.verify(mockEmployeeService, Mockito.only()).getById(id);
	}

	@Test
	public void oldEmployeeHasSimpleStateRequestHasExtendedState() {
		String id = UUID.randomUUID().toString();
		Employee oldEmployee = TestUtil.createEmployee(1, Status.ADDED);
		Assertions.assertFalse(oldEmployee.getEmployeeState() instanceof ExtendedEmployeeState);

		EmployeeStatusUpdateRequest request = new EmployeeStatusUpdateRequest();
		request.setId(id);
		request.setState(Status.IN_CHECK);
		request.setSecurityCheckStatus(SecurityCheckStatus.SECURITY_CHECK_FINISHED);
		request.setWorkPermitStatus(WorkPermitStatus.WORK_PERMIT_CHECK_FINISHED);

		Mockito.doReturn(oldEmployee).when(mockEmployeeService).getById(id);

		Employee employee = this.converter.convert(request);

		Assertions.assertNotNull(employee);
		Assertions.assertEquals(oldEmployee.getFullName(), employee.getFullName());
		Assertions.assertEquals(oldEmployee.getUsername(), employee.getUsername());
		Assertions.assertEquals(oldEmployee.getId(), employee.getId());
		Assertions.assertEquals(Status.IN_CHECK, employee.getEmployeeState().getStatus());
		Assertions.assertEquals(SecurityCheckStatus.SECURITY_CHECK_FINISHED, ((ExtendedEmployeeState)employee.getEmployeeState()).getSecurityCheckStatus());
		Assertions.assertEquals(WorkPermitStatus.WORK_PERMIT_CHECK_FINISHED, ((ExtendedEmployeeState)employee.getEmployeeState()).getWorkPermitStatus());
	}

	@Test
	public void oldEmployeeHasExtendedStateRequestHasExtendedState() {
		String id = UUID.randomUUID().toString();
		Employee oldEmployee = TestUtil.createEmployee(1, Status.IN_CHECK);
		ExtendedEmployeeState employeeState = (ExtendedEmployeeState)oldEmployee.getEmployeeState();
		employeeState.setWorkPermitStatus(WorkPermitStatus.WORK_PERMIT_CHECK_STARTED);
		employeeState.setSecurityCheckStatus(SecurityCheckStatus.SECURITY_CHECK_STARTED);

		EmployeeStatusUpdateRequest request = new EmployeeStatusUpdateRequest();
		request.setId(id);
		request.setState(Status.IN_CHECK);
		request.setSecurityCheckStatus(SecurityCheckStatus.SECURITY_CHECK_FINISHED);
		request.setWorkPermitStatus(WorkPermitStatus.WORK_PERMIT_CHECK_FINISHED);

		Mockito.doReturn(oldEmployee).when(mockEmployeeService).getById(id);

		Employee employee = this.converter.convert(request);

		Assertions.assertNotNull(employee);
		Assertions.assertEquals(oldEmployee.getFullName(), employee.getFullName());
		Assertions.assertEquals(oldEmployee.getUsername(), employee.getUsername());
		Assertions.assertEquals(oldEmployee.getId(), employee.getId());
		Assertions.assertEquals(Status.IN_CHECK, employee.getEmployeeState().getStatus());
		Assertions.assertEquals(SecurityCheckStatus.SECURITY_CHECK_FINISHED, ((ExtendedEmployeeState)employee.getEmployeeState()).getSecurityCheckStatus());
		Assertions.assertEquals(WorkPermitStatus.WORK_PERMIT_CHECK_FINISHED, ((ExtendedEmployeeState)employee.getEmployeeState()).getWorkPermitStatus());
	}

	@Test
	public void requestWithExtendedStateMissingWorkPermitStatus() {
		String id = UUID.randomUUID().toString();
		Employee oldEmployee = TestUtil.createEmployee(1, Status.IN_CHECK);
		ExtendedEmployeeState oldEmployeeEmployeeState = (ExtendedEmployeeState)oldEmployee.getEmployeeState();
		oldEmployeeEmployeeState.setWorkPermitStatus(WorkPermitStatus.WORK_PERMIT_CHECK_STARTED);
		oldEmployeeEmployeeState.setSecurityCheckStatus(SecurityCheckStatus.SECURITY_CHECK_STARTED);

		EmployeeStatusUpdateRequest request = new EmployeeStatusUpdateRequest();
		request.setId(id);
		request.setState(Status.IN_CHECK);
		request.setWorkPermitStatus(WorkPermitStatus.WORK_PERMIT_CHECK_FINISHED);

		Mockito.doReturn(oldEmployee).when(mockEmployeeService).getById(id);

		Employee employee = this.converter.convert(request);

		Assertions.assertNotNull(employee);
		Assertions.assertEquals(oldEmployee.getFullName(), employee.getFullName());
		Assertions.assertEquals(oldEmployee.getUsername(), employee.getUsername());
		Assertions.assertEquals(oldEmployee.getId(), employee.getId());
		Assertions.assertEquals(Status.IN_CHECK, employee.getEmployeeState().getStatus());
		Assertions.assertEquals(WorkPermitStatus.WORK_PERMIT_CHECK_FINISHED, ((ExtendedEmployeeState)employee.getEmployeeState()).getWorkPermitStatus());
		Assertions.assertEquals(oldEmployeeEmployeeState.getSecurityCheckStatus(), ((ExtendedEmployeeState)employee.getEmployeeState()).getSecurityCheckStatus());
	}

	@Test
	public void requestWithExtendedStateMissingSecurityCheckStatus() {
		String id = UUID.randomUUID().toString();
		Employee oldEmployee = TestUtil.createEmployee(1, Status.IN_CHECK);
		ExtendedEmployeeState oldEmployeeEmployeeState = (ExtendedEmployeeState)oldEmployee.getEmployeeState();
		oldEmployeeEmployeeState.setWorkPermitStatus(WorkPermitStatus.WORK_PERMIT_CHECK_STARTED);
		oldEmployeeEmployeeState.setSecurityCheckStatus(SecurityCheckStatus.SECURITY_CHECK_STARTED);

		EmployeeStatusUpdateRequest request = new EmployeeStatusUpdateRequest();
		request.setId(id);
		request.setState(Status.IN_CHECK);
		request.setSecurityCheckStatus(SecurityCheckStatus.SECURITY_CHECK_FINISHED);

		Mockito.doReturn(oldEmployee).when(mockEmployeeService).getById(id);

		Employee employee = this.converter.convert(request);

		Assertions.assertNotNull(employee);
		Assertions.assertEquals(oldEmployee.getFullName(), employee.getFullName());
		Assertions.assertEquals(oldEmployee.getUsername(), employee.getUsername());
		Assertions.assertEquals(oldEmployee.getId(), employee.getId());
		Assertions.assertEquals(Status.IN_CHECK, employee.getEmployeeState().getStatus());
		Assertions.assertEquals(SecurityCheckStatus.SECURITY_CHECK_FINISHED, ((ExtendedEmployeeState)employee.getEmployeeState()).getSecurityCheckStatus());
		Assertions.assertEquals(oldEmployeeEmployeeState.getWorkPermitStatus(), ((ExtendedEmployeeState)employee.getEmployeeState()).getWorkPermitStatus());
	}

	@Test
	public void oldEmployeeHasExtendedStateRequestBasicState() {
		String id = UUID.randomUUID().toString();
		Employee oldEmployee = TestUtil.createEmployee(1, Status.IN_CHECK);
		ExtendedEmployeeState employeeState = (ExtendedEmployeeState)oldEmployee.getEmployeeState();
		employeeState.setWorkPermitStatus(WorkPermitStatus.WORK_PERMIT_CHECK_STARTED);
		employeeState.setSecurityCheckStatus(SecurityCheckStatus.SECURITY_CHECK_STARTED);
		
		EmployeeStatusUpdateRequest request = new EmployeeStatusUpdateRequest();
		request.setId(id);
		request.setState(Status.APPROVED);
		
		Mockito.doReturn(oldEmployee).when(mockEmployeeService).getById(id);
		
		Employee employee = this.converter.convert(request);
		
		Assertions.assertNotNull(employee);
		Assertions.assertEquals(oldEmployee.getFullName(), employee.getFullName());
		Assertions.assertEquals(oldEmployee.getUsername(), employee.getUsername());
		Assertions.assertEquals(oldEmployee.getId(), employee.getId());
		Assertions.assertEquals(Status.APPROVED, employee.getEmployeeState().getStatus());
		Assertions.assertFalse(employee.getEmployeeState() instanceof ExtendedEmployeeState);
	}
}