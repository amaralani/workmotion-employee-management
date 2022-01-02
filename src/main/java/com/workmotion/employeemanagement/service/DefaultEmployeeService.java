package com.workmotion.employeemanagement.service;

import com.workmotion.employeemanagement.domain.Employee;
import com.workmotion.employeemanagement.repository.EmployeeRepository;
import com.workmotion.employeemanagement.state.EmployeeStateProvider;
import com.workmotion.employeemanagement.state.Status;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class DefaultEmployeeService implements EmployeeService {
	private final EmployeeRepository employeeRepository;

	public DefaultEmployeeService(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	public String createEmployee(String name, String username) {
		return employeeRepository.saveEmployee(Employee.builder()
				.username(username)
				.fullName(name)
				.employeeState(EmployeeStateProvider.getByStatus(Status.ADDED))
				.build());
	}

	public void updateEmployee(Employee employee) throws IllegalStateChangeException {
		Employee oldData = getById(employee.getId());
		if (Objects.isNull(oldData)) {
			throw new InvalidEmployeeIdException("No employee found with this id.");
		} else if (!oldData.getEmployeeState().canChangeTo(employee.getEmployeeState())) {
			throw new IllegalStateChangeException("This employee state change is not allowed.");
		} else {
			employeeRepository.saveEmployee(employee);
		}
	}

	public Employee getById(String id) {
		return employeeRepository.getById(id);
	}

	public List<Employee> getAll() {
		return employeeRepository.getAll();
	}
}