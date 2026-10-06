package com.hospital.doctorservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.hospital.doctorservice.dto.response.ApiResponseDTO;
import com.hospital.doctorservice.dto.response.HospitalResponseDTO;

@FeignClient(name = "hospital-service")
public interface HospitalClient {

    @GetMapping("/api/hospitals/{id}")
    ApiResponseDTO<HospitalResponseDTO> getHospitalById(@PathVariable("id") Long id);
}