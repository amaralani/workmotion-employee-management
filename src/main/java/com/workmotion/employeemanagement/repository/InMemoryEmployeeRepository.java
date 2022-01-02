package com.workmotion.employeemanagement.repository;

import com.workmotion.employeemanagement.domain.Employee;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * In-memory implementation of {@link EmployeeRepository}.
 */
@Repository
public class InMemoryEmployeeRepository implements EmployeeRepository {
	private static final ConcurrentMap<String, Employee> employees = new ConcurrentHashMap<>();

	public String saveEmployee(Employee employee) {
		if (!StringUtils.hasText(employee.getId())) {
			employee.setId(UUID.randomUUID().toString());
		}

		employees.put(employee.getId(), employee);
		return employee.getId();
	}

	public Employee getById(String id) {
		return employees.get(id);
	}

	public List<Employee> getAll() {
		return new ArrayList<>(employees.values());
	}
}
