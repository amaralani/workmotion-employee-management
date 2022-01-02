package com.workmotion.employeemanagement.api;

import com.workmotion.employeemanagement.domain.Employee;
import com.workmotion.employeemanagement.dto.EmployeeResponse;
import com.workmotion.employeemanagement.dto.EmployeeStatusUpdateRequest;
import com.workmotion.employeemanagement.dto.NewEmployeeRequest;
import com.workmotion.employeemanagement.service.EmployeeService;
import com.workmotion.employeemanagement.service.IllegalStateChangeException;
import com.workmotion.employeemanagement.service.InvalidEmployeeIdException;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Provides REST endpoints for {@link Employee}.
 */
@RestController
@RequestMapping({"/employee"})
public class EmployeeApi {
	private final EmployeeService employeeService;
	private final ConversionService conversionService;

	public EmployeeApi(EmployeeService employeeService, ConversionService conversionService) {
		this.employeeService = employeeService;
		this.conversionService = conversionService;
	}

	@GetMapping
	public ResponseEntity<List<EmployeeResponse>> getEmployees() {
		return ResponseEntity.ok(employeeService.getAll().stream()
				.map((employee) -> conversionService.convert(employee, EmployeeResponse.class))
				.collect(Collectors.toList()));
	}

	@GetMapping({"/{id}"})
	public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable(name = "id") String id) {
		Employee employee = employeeService.getById(id);

		if(Objects.isNull(employee)) {
			return ResponseEntity.notFound().build();
		}

		return  ResponseEntity.ok(conversionService.convert(employee, EmployeeResponse.class));
	}

	@PostMapping
	public ResponseEntity<Void> createEmployee(@Valid @RequestBody NewEmployeeRequest newEmployeeRequest) {
		String id = employeeService.createEmployee(newEmployeeRequest.getName(), newEmployeeRequest.getUsername());
		return ResponseEntity.created(URI.create(String.format("/employee/%s", id))).build();
	}

	@PutMapping
	public ResponseEntity<String> updateEmployee(@Valid @RequestBody EmployeeStatusUpdateRequest employeeStatusUpdateRequest) {
		try {
			employeeService.updateEmployee(conversionService.convert(employeeStatusUpdateRequest, Employee.class));
			return ResponseEntity.accepted().build();
		} catch (IllegalStateChangeException var3) {
			return ResponseEntity.badRequest().body(var3.getMessage());
		}
	}

	@ExceptionHandler({InvalidEmployeeIdException.class})
	@ResponseBody
	public ResponseEntity<String> handleException(InvalidEmployeeIdException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}
}