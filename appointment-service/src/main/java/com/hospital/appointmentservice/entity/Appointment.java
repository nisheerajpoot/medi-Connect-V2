package com.hospital.appointmentservice.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// [MS-CHANGE] Appointment now lives in its own project (appointment-service) with its own database (appointment_db).
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// [MS-CHANGE] The Patient, Doctor and Hospital objects (@ManyToOne) are replaced by plain Long ids.
	//             Their data lives in patient-service, doctor-service and hospital-service (other databases),
	//             and a foreign key cannot cross databases.
	@Column(name = "patient_id", nullable = false)
	private Long patientId;

	@Column(name = "doctor_id", nullable = false)
	private Long doctorId;

	@Column(name = "hospital_id", nullable = false)
	private Long hospitalId;

	private LocalDate appointmentDate;

	private LocalTime startTime;

	private LocalTime endTime;

	private String healthIssue;

	@Enumerated(EnumType.STRING)
	private AppointmentStatus status;
}