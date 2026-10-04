package com.hospital.patientservice.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.hospital.patientservice.dto.request.PatientRequestDTO;
import com.hospital.patientservice.dto.request.UpdatePatientRequestDTO;
import com.hospital.patientservice.dto.response.PatientResponseDTO;
import com.hospital.patientservice.entity.Patient;
import com.hospital.patientservice.exception.ResourceNotFoundException;
import com.hospital.patientservice.repository.PatientRepository;
import com.hospital.patientservice.service.PatientService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientServiceImpl implements PatientService {

	private final PatientRepository patientRepository;

	@Override
	public PatientResponseDTO createPatient(PatientRequestDTO requestDTO) {
		Patient patient = Patient.builder()
				.name(requestDTO.getName())
				.age(requestDTO.getAge())
				.phoneNumber(requestDTO.getPhoneNumber())
				.address(requestDTO.getAddress())
				.build();

		Patient savedPatient = patientRepository.save(patient);

		return mapToResponseDTO(savedPatient);
	}

	@Override
	public PatientResponseDTO getPatientById(Long id) {
		Patient patient = patientRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Patient", "id", id));
		return mapToResponseDTO(patient);
	}

	@Override
	public List<PatientResponseDTO> getAllPatients() {
		List<Patient> patients = patientRepository.findAll();
		List<PatientResponseDTO> patientResponse = new ArrayList<>();
		for (Patient patient : patients) {
			patientResponse.add(mapToResponseDTO(patient));
		}
		return patientResponse;
	}

	@Override
	public PatientResponseDTO updatePatient(Long id, UpdatePatientRequestDTO requestDTO) {

		Patient patient = patientRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Patient", "id", id));

		if (requestDTO.getName() == null &&
				requestDTO.getAge() == null &&
				requestDTO.getPhoneNumber() == null &&
				requestDTO.getAddress() == null) {

			throw new IllegalArgumentException("At least one field must be provided for update");
		}

		if (requestDTO.getName() != null) {
			if (requestDTO.getName().isBlank()) {
				throw new IllegalArgumentException("Name cannot be blank");
			}
			patient.setName(requestDTO.getName().trim());
		}

		if (requestDTO.getAge() != null) {
			patient.setAge(requestDTO.getAge());
		}

		if (requestDTO.getPhoneNumber() != null) {
			if (requestDTO.getPhoneNumber().isBlank()) {
				throw new IllegalArgumentException("Phone cannot be blank");
			}
			patient.setPhoneNumber(requestDTO.getPhoneNumber().trim());
		}

		if (requestDTO.getAddress() != null) {
			if (requestDTO.getAddress().isBlank()) {
				throw new IllegalArgumentException("Address cannot be blank");
			}
			patient.setAddress(requestDTO.getAddress().trim());
		}

		Patient updatedPatient = patientRepository.save(patient);

		return mapToResponseDTO(updatedPatient);
	}

	@Override
	public void deletePatient(Long id) {
		Patient patient = patientRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Patient", "id", id));
		patientRepository.delete(patient);
	}

	private PatientResponseDTO mapToResponseDTO(Patient patient) {
		return PatientResponseDTO.builder()
				.id(patient.getId())
				.name(patient.getName())
				.age(patient.getAge())
				.phoneNumber(patient.getPhoneNumber())
				.address(patient.getAddress())
				.build();
	}
}