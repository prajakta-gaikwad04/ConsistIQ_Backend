package com.may26.exception;

public class TaskNotFoundException extends RuntimeException {
	public TaskNotFoundException(String message) {
		super(message);
	}
}
