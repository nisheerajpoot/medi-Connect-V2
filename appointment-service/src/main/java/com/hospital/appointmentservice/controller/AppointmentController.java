package com.hospital.appointmentservice.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.appointmentservice.dto.request.AppointmentRequestDTO;
import com.hospital.appointmentservice.dto.request.UpdateAppointmentRequestDTO;
import com.hospital.appointmentservice.dto.response.ApiResponseDTO;
import com.hospital.appointmentservice.dto.response.AppointmentResponseDTO;
import com.hospital.appointmentservice.service.AppointmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AppointmentController {

    private final AppointmentService appointmentService;

    
    @PostMapping
    public ResponseEntity<ApiResponseDTO<AppointmentResponseDTO>> createAppointment(
            @Valid @RequestBody AppointmentRequestDTO requestDTO) {
        AppointmentResponseDTO response = appointmentService.createAppointment(requestDTO);
        ApiResponseDTO<AppointmentResponseDTO> apiResponse =
                ApiResponseDTO.success("Appointment created successfully", response);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<AppointmentResponseDTO>> getAppointmentById(@PathVariable Long id) {
        AppointmentResponseDTO appointment = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(ApiResponseDTO.success(appointment));
    }

    
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<AppointmentResponseDTO>>> getAllAppointments() {
        List<AppointmentResponseDTO> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(ApiResponseDTO.success("Fetched " + appointments.size() + " appointments", appointments));
    }

   
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<ApiResponseDTO<List<AppointmentResponseDTO>>> getAppointmentsByPatient(@PathVariable Long patientId) {
        List<AppointmentResponseDTO> appointments = appointmentService.getAppointmentsByPatient(patientId);
        return ResponseEntity.ok(ApiResponseDTO.success(appointments));
    }

    
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<ApiResponseDTO<List<AppointmentResponseDTO>>> getAppointmentsByDoctor(@PathVariable Long doctorId) {
        List<AppointmentResponseDTO> appointments = appointmentService.getAppointmentsByDoctor(doctorId);
        return ResponseEntity.ok(ApiResponseDTO.success(appointments));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<AppointmentResponseDTO>> updateAppointment(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAppointmentRequestDTO requestDTO) {
        AppointmentResponseDTO updated = appointmentService.updateAppointment(id, requestDTO);
        return ResponseEntity.ok(ApiResponseDTO.success("Appointment updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Appointment deleted successfully"));
    }
}