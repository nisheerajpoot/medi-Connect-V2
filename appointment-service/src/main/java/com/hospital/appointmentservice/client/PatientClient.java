package com.hospital.appointmentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.hospital.appointmentservice.dto.response.ApiResponseDTO;
import com.hospital.appointmentservice.dto.response.PatientResponseDTO;

@FeignClient(name = "patient-service")
public interface PatientClient {
    @GetMapping("/api/patients/{id}")
    ApiResponseDTO<PatientResponseDTO> getPatientById(@PathVariable("id") Long id);
}
