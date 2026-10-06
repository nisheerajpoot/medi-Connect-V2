package com.hospital.doctorservice.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hospital.doctorservice.client.HospitalClient;
import com.hospital.doctorservice.dto.request.DoctorRequestDTO;
import com.hospital.doctorservice.dto.request.UpdateDoctorRequestDTO;
import com.hospital.doctorservice.dto.response.DoctorResponseDTO;
import com.hospital.doctorservice.entity.Doctor;
import com.hospital.doctorservice.exception.DuplicateResourceException;
import com.hospital.doctorservice.exception.ResourceNotFoundException;
import com.hospital.doctorservice.repository.DoctorRepository;
import com.hospital.doctorservice.service.DoctorService;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DoctorServiceImpl implements DoctorService {

	public final DoctorRepository doctorRepository;
	public final HospitalClient hospitalClient;
	
	private void checkHospitalExists(Long hospitalId) {
	    try {
	        hospitalClient.getHospitalById(hospitalId);
	    } catch (FeignException.NotFound e) {
	        throw new ResourceNotFoundException("Hospital", "id", hospitalId);
	    }
	}

	@Override
	public DoctorResponseDTO createDoctor(DoctorRequestDTO requestDTO) {
		if (doctorRepository.existsByPhoneNumber(requestDTO.getPhoneNumber())) {
			throw new DuplicateResourceException("Doctor", "phoneNumber", requestDTO.getPhoneNumber());
		}

		checkHospitalExists(requestDTO.getHospitalId());
		Doctor doctor = Doctor.builder()
				.name(requestDTO.getName())
				.specialization(requestDTO.getSpecialization())
				.experience(requestDTO.getExperience())
				.phoneNumber(requestDTO.getPhoneNumber())
				.hospitalId(requestDTO.getHospitalId())
				.build();

		Doctor savedDoctor = doctorRepository.save(doctor);
		return mapToResponseDTO(savedDoctor);
	}

	@Override
	public DoctorResponseDTO updateDoctor(Long id, UpdateDoctorRequestDTO requestDTO) {

		Doctor doctor = doctorRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id));

		if (requestDTO.getName() == null &&
				requestDTO.getSpecialization() == null &&
				requestDTO.getPhoneNumber() == null &&
				requestDTO.getExperience() == null &&
				requestDTO.getHospitalId() == null) {
			throw new IllegalArgumentException("At least one field must be provided for update");
		}
		
		
		if(requestDTO.getHospitalId()!=null) {
			 // [MS-CHANGE / DONE Phase 2] New hospital is validated through hospital-service (Feign).
		    checkHospitalExists(requestDTO.getHospitalId());
		    doctor.setHospitalId(requestDTO.getHospitalId());
		}
		
		if (requestDTO.getName() != null) {
			if (requestDTO.getName().isBlank()) {
				throw new IllegalArgumentException("Name cannot be blank");
			}
			doctor.setName(requestDTO.getName().trim());
		}

		if (requestDTO.getSpecialization() != null) {
			if (requestDTO.getSpecialization().isBlank()) {
				throw new IllegalArgumentException("Specialization cannot be blank");
			}
			doctor.setSpecialization(requestDTO.getSpecialization().trim());
		}

		if (requestDTO.getExperience() != null) {
			doctor.setExperience(requestDTO.getExperience());
		}

		if (requestDTO.getPhoneNumber() != null) {
			if (requestDTO.getPhoneNumber().isBlank()) {
				throw new IllegalArgumentException("Phone cannot be blank");
			}
			String newPhoneNumber = requestDTO.getPhoneNumber().trim();
			if (!newPhoneNumber.equals(doctor.getPhoneNumber()) &&
					doctorRepository.existsByPhoneNumber(newPhoneNumber)) {
				throw new DuplicateResourceException("Doctor", "phoneNumber", newPhoneNumber);
			}
			doctor.setPhoneNumber(newPhoneNumber);
		}

		if (requestDTO.getHospitalId() != null) {
			// [MS-CHANGE / TODO Phase 2] validate the new hospitalId through hospital-service (Feign)
			doctor.setHospitalId(requestDTO.getHospitalId());
		}

		Doctor updatedDoctor = doctorRepository.save(doctor);
		return mapToResponseDTO(updatedDoctor);
	}

	@Override
	public void deleteDoctor(Long id) {
		Doctor doctor = doctorRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id));
		doctorRepository.delete(doctor);
	}

	@Override
	public List<DoctorResponseDTO> getDoctorsByHospital(Long hospitalId) {
		List<Doctor> doctors = doctorRepository.findByHospitalId(hospitalId);
		List<DoctorResponseDTO> responseList = new ArrayList<>();
		for (Doctor doctor : doctors) {
			responseList.add(mapToResponseDTO(doctor));
		}
		return responseList;
	}

	@Override
	public DoctorResponseDTO getDoctorById(Long id) {
		Doctor doctor = doctorRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id));
		return mapToResponseDTO(doctor);
	}

	@Override
	@Transactional(readOnly = true)
	public List<DoctorResponseDTO> getAllDoctors() {
		List<Doctor> doctors = doctorRepository.findAll();
		List<DoctorResponseDTO> responseList = new ArrayList<>();
		for (Doctor doctor : doctors) {
			responseList.add(mapToResponseDTO(doctor));
		}
		return responseList;
	}

	private DoctorResponseDTO mapToResponseDTO(Doctor doctor) {
		return DoctorResponseDTO.builder()
				.id(doctor.getId())
				.name(doctor.getName())
				.specialization(doctor.getSpecialization())
				.experience(doctor.getExperience())
				.phoneNumber(doctor.getPhoneNumber())
				.hospitalId(doctor.getHospitalId())
				.build();
	}
}