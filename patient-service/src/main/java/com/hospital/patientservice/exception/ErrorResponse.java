package com.hospital.patientservice.exception;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class ErrorResponse {
	private LocalDateTime timeStamp;
	private int status;
	private String error;
	private String message;
	private String path;
	private Map<String, String> validationError;

	public ErrorResponse(LocalDateTime timeStamp, int status, String error, String message, String path,
			Map<String, String> validationError) {

		this.timeStamp = timeStamp;
		this.status = status;
		this.error = error;
		this.message = message;
		this.path = path;
		this.validationError = validationError;
	}
}