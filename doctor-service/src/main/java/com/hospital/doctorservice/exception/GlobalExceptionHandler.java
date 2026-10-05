package com.hospital.doctorservice.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.servlet.http.HttpServletRequest;

// [MS-CHANGE] Every microservice keeps its own copy of exception handling, so each service stays independent.
@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
			ResourceNotFoundException ex, HttpServletRequest request) {

		ErrorResponse errorResponse = ErrorResponse.builder()
				.timeStamp(LocalDateTime.now())
				.status(HttpStatus.NOT_FOUND.value())
				.error("Not Found")
				.message(ex.getMessage())
				.path(request.getRequestURI())
				.build();

		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(DuplicateResourceException.class)
	public ResponseEntity<ErrorResponse> handleDuplicateResourceException(
			DuplicateResourceException ex, HttpServletRequest request) {

		ErrorResponse errorResponse = ErrorResponse.builder()
				.timeStamp(LocalDateTime.now())
				.status(HttpStatus.CONFLICT.value())
				.error("Conflict")
				.message(ex.getMessage())
				.path(request.getRequestURI())
				.build();

		return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(InvalidOperationException.class)
	public ResponseEntity<ErrorResponse> handleInvalidOperationException(
			InvalidOperationException ex, HttpServletRequest request) {

		ErrorResponse errorResponse = ErrorResponse.builder()
				.timeStamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value())
				.error("Invalid Operation")
				.message(ex.getMessage())
				.path(request.getRequestURI())
				.build();

		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(
			MethodArgumentNotValidException ex, HttpServletRequest request) {

		Map<String, String> validationErrors = new HashMap<>();
		for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
			validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
		}

		ErrorResponse errorResponse = ErrorResponse.builder()
				.timeStamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value())
				.error("Validation Failed")
				.message("Input validation failed. Please check the errors.")
				.path(request.getRequestURI())
				.validationError(validationErrors)
				.build();

		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleTypeMismatchException(
			MethodArgumentTypeMismatchException ex, HttpServletRequest request) {

		String message = String.format("Invalid value '%s' for parameter '%s'. Expected type: %s",
				ex.getValue(), ex.getName(),
				ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown");

		ErrorResponse errorResponse = ErrorResponse.builder()
				.timeStamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value())
				.error("Type Mismatch")
				.message(message)
				.path(request.getRequestURI())
				.build();

		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
			IllegalArgumentException ex, HttpServletRequest request) {

		ErrorResponse errorResponse = ErrorResponse.builder()
				.timeStamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value())
				.error("Bad Request")
				.message(ex.getMessage())
				.path(request.getRequestURI())
				.build();

		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGlobalException(
			Exception ex, HttpServletRequest request) {

		log.error("Unexpected error on {}", request.getRequestURI(), ex);

		ErrorResponse errorResponse = ErrorResponse.builder()
				.timeStamp(LocalDateTime.now())
				.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.error("Internal Server Error")
				.message("An unexpected error occurred. Please try again later.")
				.path(request.getRequestURI())
				.build();

		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}