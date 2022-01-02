package com.workmotion.employeemanagement.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.workmotion.employeemanagement.state.SecurityCheckStatus;
import com.workmotion.employeemanagement.state.Status;
import com.workmotion.employeemanagement.state.WorkPermitStatus;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@JsonNaming(SnakeCaseStrategy.class)
public class EmployeeStatusUpdateRequest {
	@NotBlank
	private String id;
	@NotNull
	private Status state;
	private WorkPermitStatus workPermitStatus;
	private SecurityCheckStatus securityCheckStatus;
}
