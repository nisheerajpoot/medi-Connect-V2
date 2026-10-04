package com.hospital.appointmentservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hospital.appointmentservice.service.ReceptionService;
import com.hospital.appointmentservice.dto.request.UpdateAppointmentStatusRequestDTO;
import com.hospital.appointmentservice.dto.response.ApiResponseDTO;
import com.hospital.appointmentservice.dto.response.AppointmentResponseDTO;
import com.hospital.appointmentservice.dto.response.ReceptionDashboardResponseDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reception")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReceptionController {

	  private final ReceptionService receptionService;


	    @GetMapping("/{hospitalId}")
	    public ResponseEntity<ApiResponseDTO<ReceptionDashboardResponseDTO>> getDashboard(@PathVariable Long hospitalId) {
	        ReceptionDashboardResponseDTO dashboard = receptionService.getDashboard(hospitalId);
	        return ResponseEntity.ok(ApiResponseDTO.success(dashboard));
	    }


	    @GetMapping("/{hospitalId}/appointments")
	    public ResponseEntity<ApiResponseDTO<List<AppointmentResponseDTO>>> getAppointmentsByHospital(@PathVariable Long hospitalId) {
	        List<AppointmentResponseDTO> appointments = receptionService.getAppointmentsByHospital(hospitalId);
	        return ResponseEntity.ok(ApiResponseDTO.success("Fetched " + appointments.size() + " appointments", appointments));
	    }


	    @GetMapping("/{hospitalId}/appointments/{appointmentId}")
	    public ResponseEntity<ApiResponseDTO<AppointmentResponseDTO>> getAppointmentDetail(@PathVariable Long hospitalId,@PathVariable Long appointmentId) {
	        AppointmentResponseDTO appointment = receptionService.getAppointmentDetail(hospitalId, appointmentId);
	        return ResponseEntity.ok(ApiResponseDTO.success(appointment));
	    }


	    @GetMapping("/{hospitalId}/patients/{patientId}/appointments")
	    public ResponseEntity<ApiResponseDTO<List<AppointmentResponseDTO>>> getAppointmentsByPatient(@PathVariable Long hospitalId,@PathVariable Long patientId) {
	        List<AppointmentResponseDTO> appointments =receptionService.getAppointmentsByHospitalAndPatient(hospitalId, patientId);
	        return ResponseEntity.ok(ApiResponseDTO.success(appointments));
	    }


	    @PatchMapping("/{hospitalId}/appointments/{appointmentId}/status")
	    public ResponseEntity<ApiResponseDTO<AppointmentResponseDTO>> updateAppointmentStatus(
	            @PathVariable Long hospitalId,
	            @PathVariable Long appointmentId,
	            @Valid @RequestBody UpdateAppointmentStatusRequestDTO requestDTO) {
	        AppointmentResponseDTO response =receptionService.updateAppointmentStatus(hospitalId, appointmentId, requestDTO);
	        return ResponseEntity.ok(ApiResponseDTO.success("Appointment status updated successfully", response));
	    }
	}