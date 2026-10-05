package com.hospital.appointmentservice.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hospital.appointmentservice.client.DoctorClient;
import com.hospital.appointmentservice.client.HospitalClient;
import com.hospital.appointmentservice.client.PatientClient;
import com.hospital.appointmentservice.dto.request.AppointmentRequestDTO;
import com.hospital.appointmentservice.dto.request.UpdateAppointmentRequestDTO;
import com.hospital.appointmentservice.dto.response.AppointmentResponseDTO;
import com.hospital.appointmentservice.dto.response.DoctorResponseDTO;
import com.hospital.appointmentservice.entity.Appointment;
import com.hospital.appointmentservice.entity.AppointmentStatus;
import com.hospital.appointmentservice.exception.InvalidOperationException;
import com.hospital.appointmentservice.exception.ResourceNotFoundException;
import com.hospital.appointmentservice.repository.AppointmentRepository;
import com.hospital.appointmentservice.service.AppointmentService;

import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

	// [MS-CHANGE] PatientRepository, DoctorRepository and HospitalRepository are removed.
	//             Those tables are not in this database anymore.
	private final AppointmentRepository appointmentRepository;

	// [MS-CHANGE / DONE Phase 2] Feign clients: this service now asks the other services
	//             (found by name through Eureka) instead of reading their tables.
	private final HospitalClient hospitalClient;
	private final PatientClient patientClient;
	private final DoctorClient doctorClient;

	// ---------- helper methods (Feign calls) ----------
	private void checkHospitalExists(Long hospitalId) {
	    try {
	        hospitalClient.getHospitalById(hospitalId);
	    } catch (FeignException.NotFound e) {
	        throw new ResourceNotFoundException("Hospital", "id", hospitalId);
	    }
	}

	private void checkPatientExists(Long patientId) {
	    try {
	        patientClient.getPatientById(patientId);
	    } catch (FeignException.NotFound e) {
	        throw new ResourceNotFoundException("Patient", "id", patientId);
	    }
	}

	private DoctorResponseDTO getDoctorOrThrow(Long doctorId) {
	    try {
	        return doctorClient.getDoctorById(doctorId).getData();
	    } catch (FeignException.NotFound e) {
	        throw new ResourceNotFoundException("Doctor", "id", doctorId);
	    }
	}

	// ---------- service methods ----------

	@Override
	public AppointmentResponseDTO createAppointment(AppointmentRequestDTO requestDTO) {

		// [MS-CHANGE / DONE Phase 2] Hospital, patient and doctor are validated through
		//             hospital-service, patient-service and doctor-service using Feign.
		checkHospitalExists(requestDTO.getHospitalId());
		checkPatientExists(requestDTO.getPatientId());
		DoctorResponseDTO doctor = getDoctorOrThrow(requestDTO.getDoctorId());

		if (!doctor.getHospitalId().equals(requestDTO.getHospitalId())) {
			throw new InvalidOperationException("Doctor does not belong to the selected hospital");
		}

		Appointment appointment = Appointment.builder()
				.patientId(requestDTO.getPatientId())
				.doctorId(requestDTO.getDoctorId())
				.hospitalId(requestDTO.getHospitalId())
				.appointmentDate(requestDTO.getAppointmentDate())
				.healthIssue(requestDTO.getHealthIssue())
				.status(AppointmentStatus.PENDING)
				.build();

		Appointment saveAppointment = appointmentRepository.save(appointment);

		return mapToResponseDTO(saveAppointment);
	}

	@Override
	public AppointmentResponseDTO getAppointmentById(Long id) {
		Appointment appointment = appointmentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", id));
		return mapToResponseDTO(appointment);
	}

	@Override
	public List<AppointmentResponseDTO> getAllAppointments() {
		List<Appointment> appointments = appointmentRepository.findAll();
		List<AppointmentResponseDTO> responseList = new ArrayList<>();
		for (Appointment appointment : appointments) {
			responseList.add(mapToResponseDTO(appointment));
		}
		return responseList;
	}

	@Override
	public List<AppointmentResponseDTO> getAppointmentsByPatient(Long patientId) {
		// [MS-CHANGE / DONE Phase 2] Patient existence is checked through patient-service (Feign).
		checkPatientExists(patientId);

		List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);
		List<AppointmentResponseDTO> responseList = new ArrayList<>();
		for (Appointment appointment : appointments) {
			responseList.add(mapToResponseDTO(appointment));
		}
		return responseList;
	}

	@Override
	public List<AppointmentResponseDTO> getAppointmentsByDoctor(Long doctorId) {
		// [MS-CHANGE / DONE Phase 2] Doctor existence is checked through doctor-service (Feign).
		getDoctorOrThrow(doctorId);

		List<Appointment> appointments = appointmentRepository.findByDoctorId(doctorId);
		List<AppointmentResponseDTO> responseList = new ArrayList<>();
		for (Appointment appointment : appointments) {
			responseList.add(mapToResponseDTO(appointment));
		}
		return responseList;
	}

	@Override
	public AppointmentResponseDTO updateAppointment(Long id, UpdateAppointmentRequestDTO requestDTO) {
		Appointment appointment = appointmentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", id));

		// Once confirmed or rejected, the appointment is locked (avoids clashing with a confirmed time slot)
		if (appointment.getStatus() != AppointmentStatus.PENDING) {
			throw new InvalidOperationException("Only PENDING appointments can be updated");
		}
		if (requestDTO.getDoctorId() == null
				&& requestDTO.getAppointmentDate() == null
				&& requestDTO.getHealthIssue() == null) {
			throw new IllegalArgumentException("At least one field must be provided for update");
		}
		if (requestDTO.getDoctorId() != null) {
			// [MS-CHANGE / DONE Phase 2] The new doctor must exist AND belong to this appointment's
			//             hospital. doctor-service gives us the doctor, and we compare its hospitalId.
			DoctorResponseDTO doctor = getDoctorOrThrow(requestDTO.getDoctorId());

			if (!doctor.getHospitalId().equals(appointment.getHospitalId())) {
				throw new InvalidOperationException("Doctor does not belong to this appointment's hospital");
			}

			appointment.setDoctorId(requestDTO.getDoctorId());
		}
		if (requestDTO.getAppointmentDate() != null) {
			appointment.setAppointmentDate(requestDTO.getAppointmentDate());
		}
		if (requestDTO.getHealthIssue() != null) {
			if (requestDTO.getHealthIssue().isBlank()) {
				throw new IllegalArgumentException("Health issue cannot be blank");
			}
			appointment.setHealthIssue(requestDTO.getHealthIssue().trim());
		}
		Appointment updated = appointmentRepository.save(appointment);
		return mapToResponseDTO(updated);
	}

	@Override
	public void deleteAppointment(Long id) {
		Appointment appointment = appointmentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", id));
		appointmentRepository.delete(appointment);
	}

	private AppointmentResponseDTO mapToResponseDTO(Appointment appointment) {
		// [MS-CHANGE] Only ids are returned. The monolith built full patient, doctor and hospital DTOs here.
		return AppointmentResponseDTO.builder()
				.id(appointment.getId())
				.patientId(appointment.getPatientId())
				.doctorId(appointment.getDoctorId())
				.hospitalId(appointment.getHospitalId())
				.appointmentDate(appointment.getAppointmentDate())
				.startTime(appointment.getStartTime())
				.endTime(appointment.getEndTime())
				.healthIssue(appointment.getHealthIssue())
				.status(appointment.getStatus())
				.build();
	}
}