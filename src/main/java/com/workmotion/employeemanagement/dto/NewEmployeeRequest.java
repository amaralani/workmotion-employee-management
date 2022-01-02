package com.workmotion.employeemanagement.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class NewEmployeeRequest {
	@NotBlank
	private String username;
	@NotBlank
	private String name;
}
