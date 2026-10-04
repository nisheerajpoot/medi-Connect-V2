package com.hospital.appointmentservice.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hospital.appointmentservice.entity.Appointment;
import com.hospital.appointmentservice.entity.AppointmentStatus;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

	List<Appointment> findByPatientId(Long patientId);

	List<Appointment> findByDoctorId(Long doctorId);
	
	List<Appointment> findByDoctorIdAndAppointmentDate(Long doctorId,LocalDate localdate);
	List<Appointment> findByHospitalId(Long hospitalId);

	List<Appointment> findByHospitalIdAndPatientId(Long hospitalId, Long patientId);

	List<Appointment> findByHospitalIdAndStatus(Long hospitalId, AppointmentStatus status);
	
}