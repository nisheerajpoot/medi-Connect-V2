package com.hospital.appointmentservice.dto.request;


import java.time.LocalDate;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentRequestDTO {
	@NotNull(message = "Patient ID is required")
	private Long patientId;

	@NotNull(message = "Doctor ID is required")
	private Long doctorId;

	@NotNull(message = "Hospital ID is required")
	private Long hospitalId;

	@NotNull(message = "Appointment date is required")
	@Future(message = "Appointment date must be in the future")
	private LocalDate appointmentDate;

	@NotBlank(message = "Health issue is required")
	private String healthIssue;
}
