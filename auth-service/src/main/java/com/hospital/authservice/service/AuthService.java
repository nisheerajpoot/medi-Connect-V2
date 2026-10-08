package com.hospital.authservice.service;

import com.hospital.authservice.dto.request.LoginRequest;
import com.hospital.authservice.dto.request.RegisterDoctorRequest;
import com.hospital.authservice.dto.request.RegisterHospitalRequest;
import com.hospital.authservice.dto.request.RegisterPatientRequest;
import com.hospital.authservice.dto.response.AuthResponseDTO;

public interface AuthService {
	AuthResponseDTO registerPatient(RegisterPatientRequest request);

	AuthResponseDTO registerDoctor(RegisterDoctorRequest request);

	AuthResponseDTO registerHospital(RegisterHospitalRequest request);

	AuthResponseDTO login(LoginRequest request);
}

