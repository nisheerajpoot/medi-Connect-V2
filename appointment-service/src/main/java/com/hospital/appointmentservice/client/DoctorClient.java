package com.hospital.appointmentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.hospital.appointmentservice.dto.response.ApiResponseDTO;
import com.hospital.appointmentservice.dto.response.DoctorResponseDTO;

@FeignClient(name="doctor-service")
public interface DoctorClient {
	@GetMapping("/api/doctors/{id}")
	ApiResponseDTO<DoctorResponseDTO> getDoctorById(@PathVariable("id") Long id);
	

}
