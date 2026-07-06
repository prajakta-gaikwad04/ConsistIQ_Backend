package com.may26.exception;

public class UnauthorizedTaskAccessException extends RuntimeException {
	public UnauthorizedTaskAccessException(String message) {
		super(message);
	}

}
