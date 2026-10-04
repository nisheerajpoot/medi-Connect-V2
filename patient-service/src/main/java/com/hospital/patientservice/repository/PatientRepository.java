package com.hospital.patientservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hospital.patientservice.entity.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {

}