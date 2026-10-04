package com.hospital.appointmentservice.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Future;
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
public class UpdateAppointmentRequestDTO {
    private Long doctorId;

    @Future(message = "Appointment date must be in the future")
    private LocalDate appointmentDate;

    private String healthIssue;
}
