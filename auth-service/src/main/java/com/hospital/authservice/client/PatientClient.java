package com.hospital.authservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.hospital.authservice.dto.request.PatientRequestDTO;
import com.hospital.authservice.dto.response.ApiResponseDTO;
import com.hospital.authservice.dto.response.PatientResponseDTO;

@FeignClient(name = "patient-service")
public interface PatientClient {

	@PostMapping("/api/patients")
	ApiResponseDTO<PatientResponseDTO> createPatient(@RequestBody PatientRequestDTO request);

	@GetMapping("/api/patients/{id}")
	ApiResponseDTO<PatientResponseDTO> getPatientById(@PathVariable("id") Long id);

	// Used to UNDO a profile if the login account could not be saved (compensation)
	@DeleteMapping("/api/patients/{id}")
	ApiResponseDTO<Void> deletePatient(@PathVariable("id") Long id);
}