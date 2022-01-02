package com.workmotion.employeemanagement.repository;

import com.workmotion.employeemanagement.domain.Employee;

import java.util.List;

/**
 * Handles all repository methods of {@link Employee}.
 */
public interface EmployeeRepository {
	String saveEmployee(Employee employee);

	Employee getById(String id);

	List<Employee> getAll();
}
