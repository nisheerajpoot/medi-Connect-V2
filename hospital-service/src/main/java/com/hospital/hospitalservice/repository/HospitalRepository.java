package com.hospital.hospitalservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hospital.hospitalservice.entity.Hospital;

public interface HospitalRepository  extends JpaRepository<Hospital, Long> {
	List<Hospital> findByNameContainingIgnoreCase(String name);
	boolean existsByPhoneNumber(String phoneNumber);
}
