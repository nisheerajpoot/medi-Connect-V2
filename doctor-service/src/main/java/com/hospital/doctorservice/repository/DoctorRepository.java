package com.hospital.doctorservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hospital.doctorservice.entity.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
	List<Doctor> findByHospitalId(Long hospitalId);
	boolean existsByPhoneNumber(String phoneNumber);
}