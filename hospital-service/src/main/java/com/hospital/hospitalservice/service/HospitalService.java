package com.hospital.hospitalservice.service;

import java.util.List;

import com.hospital.hospitalservice.dto.request.HospitalRequestDTO;
import com.hospital.hospitalservice.dto.request.UpdateHospitalRequestDTO;
import com.hospital.hospitalservice.dto.response.HospitalResponseDTO;

public interface HospitalService {
	
	HospitalResponseDTO createHospital(HospitalRequestDTO requestDTO);

	HospitalResponseDTO getHospitalById(Long id);

	List<HospitalResponseDTO> getAllHospitals();

	HospitalResponseDTO updateHospital(Long id, UpdateHospitalRequestDTO  requestDTO);

	void deleteHospital(Long id);

	List<HospitalResponseDTO> searchHospitalByName(String name);
}