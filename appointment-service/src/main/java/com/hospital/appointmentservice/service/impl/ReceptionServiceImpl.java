package com.hospital.appointmentservice.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hospital.appointmentservice.client.HospitalClient;
import com.hospital.appointmentservice.dto.request.UpdateAppointmentStatusRequestDTO;
import com.hospital.appointmentservice.dto.response.AppointmentResponseDTO;
import com.hospital.appointmentservice.dto.response.HospitalResponseDTO;
import com.hospital.appointmentservice.dto.response.ReceptionDashboardResponseDTO;
import com.hospital.appointmentservice.entity.Appointment;
import com.hospital.appointmentservice.entity.AppointmentStatus;
import com.hospital.appointmentservice.exception.InvalidOperationException;
import com.hospital.appointmentservice.exception.ResourceNotFoundException;
import com.hospital.appointmentservice.repository.AppointmentRepository;
import com.hospital.appointmentservice.service.ReceptionService;

import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ReceptionServiceImpl implements ReceptionService {

	// [MS-CHANGE] HospitalRepository is removed. Hospital data lives in hospital-service.
	private final AppointmentRepository appointmentRepository;

	// [MS-CHANGE / DONE Phase 2] Hospital details (name, opening and closing time) now come
	//             from hospital-service through Feign.
	private final HospitalClient hospitalClient;

	// ---------- helper method (Feign call) ----------

	private HospitalResponseDTO getHospitalOrThrow(Long hospitalId) {
		try {
			return hospitalClient.getHospitalById(hospitalId).getData();
		} catch (FeignException.NotFound e) {
			throw new ResourceNotFoundException("Hospital", "id", hospitalId);
		}
	}

	// ---------- service methods ----------

	@Override
	public ReceptionDashboardResponseDTO getDashboard(Long hospitalId) {
		// [MS-CHANGE / DONE Phase 2] The hospital is checked (and its name is fetched)
		//             through hospital-service using Feign.
		HospitalResponseDTO hospital = getHospitalOrThrow(hospitalId);

		long pending = appointmentRepository.findByHospitalIdAndStatus(hospitalId, AppointmentStatus.PENDING).size();
		long confirmed = appointmentRepository.findByHospitalIdAndStatus(hospitalId, AppointmentStatus.CONFIRMED).size();
		long rejected = appointmentRepository.findByHospitalIdAndStatus(hospitalId, AppointmentStatus.REJECTED).size();

		return ReceptionDashboardResponseDTO.builder()
				.hospitalId(hospitalId)
				.hospitalName(hospital.getName())
				.pendingCount(pending)
				.confirmedCount(confirmed)
				.rejectedCount(rejected)
				.build();
	}

	@Override
	public List<AppointmentResponseDTO> getAppointmentsByHospital(Long hospitalId) {
		List<Appointment> appointments = appointmentRepository.findByHospitalId(hospitalId);
		List<AppointmentResponseDTO> responseList = new ArrayList<>();
		for (Appointment appointment : appointments) {
			responseList.add(mapToResponseDTO(appointment));
		}
		return responseList;
	}

	@Override
	public AppointmentResponseDTO getAppointmentDetail(Long hospitalId, Long appointmentId) {
		Appointment appointment = appointmentRepository.findById(appointmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", appointmentId));

		// [MS-CHANGE] Same check as before, but it now reads the hospitalId field instead of the Hospital object.
		if (!appointment.getHospitalId().equals(hospitalId)) {
			throw new InvalidOperationException("This appointment does not belong to this hospital");
		}

		return mapToResponseDTO(appointment);
	}

	@Override
	public List<AppointmentResponseDTO> getAppointmentsByHospitalAndPatient(Long hospitalId, Long patientId) {
		List<Appointment> appointments = appointmentRepository.findByHospitalIdAndPatientId(hospitalId, patientId);
		List<AppointmentResponseDTO> responseList = new ArrayList<>();
		for (Appointment appointment : appointments) {
			responseList.add(mapToResponseDTO(appointment));
		}
		return responseList;
	}

	@Override
	public AppointmentResponseDTO updateAppointmentStatus(Long hospitalId, Long appointmentId,
			UpdateAppointmentStatusRequestDTO requestDTO) {

		Appointment appointment = appointmentRepository.findById(appointmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", appointmentId));

		if (requestDTO.getStatus() == AppointmentStatus.CONFIRMED) {
			if (requestDTO.getStartTime() == null || requestDTO.getEndTime() == null) {
				throw new IllegalArgumentException("Start time and end time are required to confirm an appointment");
			}

			if (!requestDTO.getStartTime().isBefore(requestDTO.getEndTime())) {
				throw new IllegalArgumentException("Start time must be before end time");
			}

			// [MS-CHANGE / DONE Phase 2] The slot must be inside the hospital's opening and closing hours.
			//             The timings come from hospital-service (Feign).
			HospitalResponseDTO hospital = getHospitalOrThrow(appointment.getHospitalId());

			if (requestDTO.getStartTime().isBefore(hospital.getOpeningTime())
					|| requestDTO.getEndTime().isAfter(hospital.getClosingTime())) {
				throw new InvalidOperationException("Appointment time must be within hospital hours ("
						+ hospital.getOpeningTime() + " - " + hospital.getClosingTime() + ")");
			}

			// Doctor overlap check still works: the data is in this service (doctorId is a plain field now).
			List<Appointment> doctorAppointments = appointmentRepository
					.findByDoctorIdAndAppointmentDate(appointment.getDoctorId(), appointment.getAppointmentDate());

			for (Appointment existing : doctorAppointments) {
				if (existing.getId().equals(appointment.getId())) {
					continue;
				}
				if (existing.getStatus() != AppointmentStatus.CONFIRMED) {
					continue;
				}
				boolean overlap = requestDTO.getStartTime().isBefore(existing.getEndTime())
						&& existing.getStartTime().isBefore(requestDTO.getEndTime());
				if (overlap) {
					throw new InvalidOperationException("Doctor already has an appointment in this time slot");
				}
			}

			appointment.setStartTime(requestDTO.getStartTime());
			appointment.setEndTime(requestDTO.getEndTime());
		}

		appointment.setStatus(requestDTO.getStatus());

		Appointment updated = appointmentRepository.save(appointment);
		return mapToResponseDTO(updated);
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