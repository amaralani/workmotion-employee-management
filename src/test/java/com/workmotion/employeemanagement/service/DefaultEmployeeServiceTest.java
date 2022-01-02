package com.workmotion.employeemanagement.service;

import com.workmotion.employeemanagement.TestUtil;
import com.workmotion.employeemanagement.domain.Employee;
import com.workmotion.employeemanagement.repository.EmployeeRepository;
import com.workmotion.employeemanagement.state.EmployeeStateProvider;
import com.workmotion.employeemanagement.state.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class DefaultEmployeeServiceTest {
	private EmployeeRepository mockEmployeeRepository;
	private EmployeeService employeeService;

	public DefaultEmployeeServiceTest() {
	}

	@BeforeEach
	public void beforeEach() {
		mockEmployeeRepository = Mockito.mock(EmployeeRepository.class);
		employeeService = new DefaultEmployeeService(mockEmployeeRepository);
	}

	@Test
	public void getById() {
		String id = UUID.randomUUID().toString();
		Employee employee = TestUtil.createEmployee(1, Status.ADDED);
		employee.setId(id);

		Mockito.when(mockEmployeeRepository.getById(id)).thenReturn(employee);

		Employee returnedEmployee = employeeService.getById(id);

		Assertions.assertEquals(employee, returnedEmployee);
		Mockito.verify(mockEmployeeRepository, Mockito.only()).getById(id);
	}

	@Test
	public void getAll() {
		String id = UUID.randomUUID().toString();
		Employee employee = TestUtil.createEmployee(1, Status.ADDED);
		employee.setId(id);

		Mockito.when(mockEmployeeRepository.getAll()).thenReturn(Collections.singletonList(employee));

		List<Employee> returnedEmployees = employeeService.getAll();

		Assertions.assertEquals(1, returnedEmployees.size());
		Assertions.assertTrue(returnedEmployees.contains(employee));
		Mockito.verify(mockEmployeeRepository, Mockito.only()).getAll();
	}

	@Test
	public void createEmployee() {
		String id = UUID.randomUUID().toString();
		Mockito.when(mockEmployeeRepository.saveEmployee(Mockito.any())).thenReturn(id);
		String returnedId = employeeService.createEmployee("fullname", "username");
		Assertions.assertEquals(id, returnedId);
		Mockito.verify(mockEmployeeRepository, Mockito.only()).saveEmployee(Mockito.any());
	}

	@Test
	public void updateEmployeeOldStateIsNotChangeableToNewStateShouldThrowException() {
		String id = UUID.randomUUID().toString();
		Employee oldEmployee = TestUtil.createEmployee(1, Status.ADDED);
		oldEmployee.setId(id);
		Employee newEmployee = TestUtil.createEmployee(1, Status.APPROVED);
		newEmployee.setId(id);

		Mockito.when(mockEmployeeRepository.getById(id)).thenReturn(oldEmployee);

		Assertions.assertThrows(IllegalStateChangeException.class, () -> employeeService.updateEmployee(newEmployee));

		Mockito.verify(mockEmployeeRepository, Mockito.times(1)).getById(id);
		Mockito.verify(mockEmployeeRepository, Mockito.never()).saveEmployee(Mockito.any());
	}

	@Test
	public void updateEmployeeOldStateIsChangeableToNewStateShouldSucceed() throws IllegalStateChangeException,
			InvalidEmployeeIdException {
		String id = UUID.randomUUID().toString();
		Employee oldEmployee = TestUtil.createEmployee(1, Status.APPROVED);
		oldEmployee.setId(id);
		Employee newEmployee = TestUtil.createEmployee(1, Status.ACTIVE);
		newEmployee.setId(id);

		Mockito.when(mockEmployeeRepository.getById(id)).thenReturn(oldEmployee);
		employeeService.updateEmployee(newEmployee);
		Mockito.verify(mockEmployeeRepository, Mockito.times(1)).getById(id);
		Mockito.verify(mockEmployeeRepository, Mockito.times(1)).saveEmployee(Mockito.any());
	}

	@Test
	public void updateEmployeeInvalidIdShouldThrowException() {
		String id = UUID.randomUUID().toString();
		Employee newEmployee = TestUtil.createEmployee(1, Status.APPROVED);
		newEmployee.setId(id);

		Mockito.when(mockEmployeeRepository.getById(id)).thenReturn(null);
		Assertions.assertThrows(InvalidEmployeeIdException.class, () -> employeeService.updateEmployee(newEmployee));
		Mockito.verify(mockEmployeeRepository, Mockito.times(1)).getById(id);
		Mockito.verify(mockEmployeeRepository, Mockito.never()).saveEmployee(Mockito.any());
	}
}