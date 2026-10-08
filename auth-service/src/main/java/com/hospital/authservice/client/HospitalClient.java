package com.hospital.authservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.hospital.authservice.dto.request.HospitalRequestDTO;
import com.hospital.authservice.dto.response.ApiResponseDTO;
import com.hospital.authservice.dto.response.HospitalResponseDTO;

@FeignClient(name = "hospital-service")
public interface HospitalClient {

	@PostMapping("/api/hospitals")
	ApiResponseDTO<HospitalResponseDTO> createHospital(@RequestBody HospitalRequestDTO request);

	@GetMapping("/api/hospitals/{id}")
	ApiResponseDTO<HospitalResponseDTO> getHospitalById(@PathVariable("id") Long id);

	@DeleteMapping("/api/hospitals/{id}")
	ApiResponseDTO<Void> deleteHospital(@PathVariable("id") Long id);
}