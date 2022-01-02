package com.workmotion.employeemanagement.service;

public class InvalidEmployeeIdException extends RuntimeException {
	public InvalidEmployeeIdException(String message) {
		super(message);
	}
}