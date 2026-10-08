package com.hospital.authservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.hospital.authservice.dto.request.DoctorRequestDTO;
import com.hospital.authservice.dto.response.ApiResponseDTO;
import com.hospital.authservice.dto.response.DoctorResponseDTO;

@FeignClient(name = "doctor-service")
public interface DoctorClient {

	@PostMapping("/api/doctors")
	ApiResponseDTO<DoctorResponseDTO> createDoctor(@RequestBody DoctorRequestDTO request);

	@GetMapping("/api/doctors/{id}")
	ApiResponseDTO<DoctorResponseDTO> getDoctorById(@PathVariable("id") Long id);

	@DeleteMapping("/api/doctors/{id}")
	ApiResponseDTO<Void> deleteDoctor(@PathVariable("id") Long id);
}