package com.hospital.doctorservice.exception;

public class InvalidOperationException extends RuntimeException {

	public InvalidOperationException(String message) {
		super(message);
	}
}