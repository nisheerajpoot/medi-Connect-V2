package com.hospital.authservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.authservice.dto.request.LoginRequest;
import com.hospital.authservice.dto.request.RegisterDoctorRequest;
import com.hospital.authservice.dto.request.RegisterHospitalRequest;
import com.hospital.authservice.dto.request.RegisterPatientRequest;
import com.hospital.authservice.dto.response.ApiResponseDTO;
import com.hospital.authservice.dto.response.AuthResponseDTO;
import com.hospital.authservice.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

	private final AuthService authService;

	@PostMapping("/register/patient")
	public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> registerPatient(
			@Valid @RequestBody RegisterPatientRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponseDTO.success("Patient registered successfully", authService.registerPatient(request)));
	}

	@PostMapping("/register/doctor")
	public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> registerDoctor(
			@Valid @RequestBody RegisterDoctorRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponseDTO.success("Doctor registered successfully", authService.registerDoctor(request)));
	}

	@PostMapping("/register/hospital")
	public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> registerHospital(
			@Valid @RequestBody RegisterHospitalRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponseDTO.success("Hospital registered successfully", authService.registerHospital(request)));
	}

	@PostMapping("/login")
	public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> login(@Valid @RequestBody LoginRequest request) {
		return ResponseEntity.ok(ApiResponseDTO.success("Login successful", authService.login(request)));
	}
}
