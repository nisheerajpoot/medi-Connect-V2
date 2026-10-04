package com.hospital.patientservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.patientservice.dto.request.PatientRequestDTO;
import com.hospital.patientservice.dto.request.UpdatePatientRequestDTO;
import com.hospital.patientservice.dto.response.ApiResponseDTO;
import com.hospital.patientservice.dto.response.PatientResponseDTO;
import com.hospital.patientservice.service.PatientService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PatientController {
	private final PatientService patientService;

	@PostMapping
	public ResponseEntity<ApiResponseDTO<PatientResponseDTO>> createPatient(
			@Valid @RequestBody PatientRequestDTO requestDTO) {
		PatientResponseDTO response = patientService.createPatient(requestDTO);
		ApiResponseDTO<PatientResponseDTO> apiResponse = ApiResponseDTO.success("Patient create successfully", response);
		return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponseDTO<PatientResponseDTO>> getPatientById(@PathVariable Long id) {
		PatientResponseDTO response = patientService.getPatientById(id);
		return ResponseEntity.ok(ApiResponseDTO.success(response));
	}

	@GetMapping
	public ResponseEntity<ApiResponseDTO<List<PatientResponseDTO>>> getAllPatients() {
		List<PatientResponseDTO> patients = patientService.getAllPatients();
		return ResponseEntity.ok(ApiResponseDTO.success("Fetched " + patients.size() + " patients", patients));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponseDTO<PatientResponseDTO>> updatePatient(
			@PathVariable Long id,
			@Valid @RequestBody UpdatePatientRequestDTO requestDTO) {

		PatientResponseDTO updatedPatient = patientService.updatePatient(id, requestDTO);

		return ResponseEntity.ok(ApiResponseDTO.success("Patient updated successfully", updatedPatient));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponseDTO<Void>> deletePatient(@PathVariable Long id) {

		patientService.deletePatient(id);

		return ResponseEntity.ok(ApiResponseDTO.success("Patient deleted successfully"));
	}
}