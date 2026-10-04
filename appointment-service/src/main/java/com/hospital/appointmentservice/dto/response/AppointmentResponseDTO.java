package com.hospital.appointmentservice.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

import com.hospital.appointmentservice.entity.AppointmentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentResponseDTO {
	private Long id;

	// [MS-CHANGE] Returns only ids (the monolith returned nested patient and doctor objects).
	//             Names and other details live in the other services.
	private Long patientId;
	private Long doctorId;
	private Long hospitalId;

	private LocalDate appointmentDate;
	private LocalTime startTime;
	private LocalTime endTime;
	private String healthIssue;
	private AppointmentStatus status;
}