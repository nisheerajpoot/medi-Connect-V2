package com.hospital.doctorservice.controller;

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

import com.hospital.doctorservice.dto.request.DoctorRequestDTO;
import com.hospital.doctorservice.dto.request.UpdateDoctorRequestDTO;
import com.hospital.doctorservice.dto.response.ApiResponseDTO;
import com.hospital.doctorservice.dto.response.DoctorResponseDTO;
import com.hospital.doctorservice.service.DoctorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

// [MS-CHANGE] Same URL (/api/doctors) as the monolith, so the frontend does not need to change.
// [MS-CHANGE] Runs on its own port (8082). Later the API Gateway will route /api/doctors/** to this service.
@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DoctorController {

	private final DoctorService doctorService;

	@PostMapping
	public ResponseEntity<ApiResponseDTO<DoctorResponseDTO>> createDoctor(
			@Valid @RequestBody DoctorRequestDTO requestDTO) {
		DoctorResponseDTO response = doctorService.createDoctor(requestDTO);
		return new ResponseEntity<>(ApiResponseDTO.success("Doctor created successfully", response), HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponseDTO<DoctorResponseDTO>> getDoctorById(@PathVariable Long id) {
		return ResponseEntity.ok(ApiResponseDTO.success(doctorService.getDoctorById(id)));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponseDTO<DoctorResponseDTO>> updateDoctor(
			@PathVariable Long id,
			@Valid @RequestBody UpdateDoctorRequestDTO requestDTO) {
		DoctorResponseDTO updated = doctorService.updateDoctor(id, requestDTO);
		return ResponseEntity.ok(ApiResponseDTO.success("Doctor updated successfully", updated));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponseDTO<Void>> deleteDoctor(@PathVariable Long id) {
		doctorService.deleteDoctor(id);
		return ResponseEntity.ok(ApiResponseDTO.success("Doctor deleted successfully"));
	}

	@GetMapping("/hospital/{hospitalId}")
	public ResponseEntity<ApiResponseDTO<List<DoctorResponseDTO>>> getDoctorsByHospital(
			@PathVariable Long hospitalId) {
		return ResponseEntity.ok(ApiResponseDTO.success(doctorService.getDoctorsByHospital(hospitalId)));
	}

	@GetMapping
	public ResponseEntity<ApiResponseDTO<List<DoctorResponseDTO>>> getAllDoctors() {
		List<DoctorResponseDTO> doctors = doctorService.getAllDoctors();
		return ResponseEntity.ok(ApiResponseDTO.success("Fetched " + doctors.size() + " doctors", doctors));
	}
}