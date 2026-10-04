package com.hospital.patientservice.service;

import java.util.List;

import com.hospital.patientservice.dto.request.PatientRequestDTO;
import com.hospital.patientservice.dto.request.UpdatePatientRequestDTO;
import com.hospital.patientservice.dto.response.PatientResponseDTO;

public interface PatientService {

	PatientResponseDTO createPatient(PatientRequestDTO requestDTO);
	PatientResponseDTO getPatientById(Long id);
	List<PatientResponseDTO> getAllPatients();
	PatientResponseDTO updatePatient(Long id, UpdatePatientRequestDTO requestDTO);
	void deletePatient(Long id);
}