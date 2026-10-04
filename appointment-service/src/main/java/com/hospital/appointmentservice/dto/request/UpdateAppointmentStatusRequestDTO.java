package com.hospital.appointmentservice.dto.request;

import java.time.LocalTime;

import com.hospital.appointmentservice.entity.AppointmentStatus;

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
public class UpdateAppointmentStatusRequestDTO {

	@NotNull(message = "Status is required")
	private AppointmentStatus status;

	private LocalTime startTime;

	private LocalTime endTime;
}
