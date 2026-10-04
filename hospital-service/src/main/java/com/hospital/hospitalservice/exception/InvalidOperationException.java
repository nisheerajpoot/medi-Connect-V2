package com.hospital.hospitalservice.exception;

public class InvalidOperationException extends RuntimeException {

	public InvalidOperationException(String message) {
		super(message);
	}
}