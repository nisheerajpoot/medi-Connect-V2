package com.hospital.appointmentservice.service;

import java.util.List;

import com.hospital.appointmentservice.dto.request.AppointmentRequestDTO;
import com.hospital.appointmentservice.dto.request.UpdateAppointmentRequestDTO;
import com.hospital.appointmentservice.dto.response.AppointmentResponseDTO;

public interface AppointmentService {
	
	AppointmentResponseDTO createAppointment(AppointmentRequestDTO requestDTO);
	AppointmentResponseDTO getAppointmentById(Long id);
	List<AppointmentResponseDTO> getAllAppointments();
	List<AppointmentResponseDTO> getAppointmentsByPatient(Long patientId);
	List<AppointmentResponseDTO> getAppointmentsByDoctor(Long doctorId);
	AppointmentResponseDTO updateAppointment(Long id, UpdateAppointmentRequestDTO requestDTO);
	void deleteAppointment (Long id);
	
}

