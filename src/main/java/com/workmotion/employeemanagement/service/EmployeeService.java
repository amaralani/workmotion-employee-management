package com.workmotion.employeemanagement.service;

import com.workmotion.employeemanagement.domain.Employee;

import java.util.List;

public interface EmployeeService {
	String createEmployee(String name, String username);

	void updateEmployee(Employee employee) throws IllegalStateChangeException;

	Employee getById(String id);

	List<Employee> getAll();
}
