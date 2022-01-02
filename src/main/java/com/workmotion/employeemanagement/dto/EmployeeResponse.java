package com.workmotion.employeemanagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * Representation of an employee to be used as a response.
 */
@Data
@JsonNaming(SnakeCaseStrategy.class)
public class EmployeeResponse {
	private String id;
	private String name;
	private String username;
	private String applicationState;
	@JsonInclude(Include.NON_EMPTY)
	private String workPermitStatus;
	@JsonInclude(Include.NON_EMPTY)
	private String securityCheckStatus;
}