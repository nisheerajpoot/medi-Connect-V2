package com.hospital.doctorservice.service;

import java.util.List;

import com.hospital.doctorservice.dto.request.DoctorRequestDTO;
import com.hospital.doctorservice.dto.request.UpdateDoctorRequestDTO;
import com.hospital.doctorservice.dto.response.DoctorResponseDTO;

public interface DoctorService {
	DoctorResponseDTO createDoctor(DoctorRequestDTO requestDTO);
	DoctorResponseDTO getDoctorById(Long id);
	List<DoctorResponseDTO> getAllDoctors();
	DoctorResponseDTO updateDoctor(Long id, UpdateDoctorRequestDTO requestDTO);
	void deleteDoctor(Long id);
	List<DoctorResponseDTO> getDoctorsByHospital(Long hospitalId);
}
