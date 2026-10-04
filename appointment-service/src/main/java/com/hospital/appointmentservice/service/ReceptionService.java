package com.hospital.appointmentservice.service;

import java.util.List;

import com.hospital.appointmentservice.dto.request.UpdateAppointmentStatusRequestDTO;
import com.hospital.appointmentservice.dto.response.AppointmentResponseDTO;
import com.hospital.appointmentservice.dto.response.ReceptionDashboardResponseDTO;

public interface ReceptionService {
	ReceptionDashboardResponseDTO getDashboard(Long hospitalId);

	List<AppointmentResponseDTO> getAppointmentsByHospital(Long hospitalId);

	AppointmentResponseDTO getAppointmentDetail(Long hospitalId, Long appointmentId);

	List<AppointmentResponseDTO> getAppointmentsByHospitalAndPatient(Long hospitalId, Long patientId);

	AppointmentResponseDTO updateAppointmentStatus(Long hospitalId, Long appointmentId, UpdateAppointmentStatusRequestDTO requestDTO);
}

