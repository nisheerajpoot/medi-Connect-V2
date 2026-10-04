package com.hospital.doctorservice.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// [MS-CHANGE] Doctor now lives in its own project (doctor-service) with its own database (doctor_db).
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(nullable = false, length = 100)
	private String specialization;

	private Integer experience;

	@Column(length = 15)
	private String phoneNumber;

	// [MS-CHANGE] The Hospital object (@ManyToOne) is replaced by hospitalId, a plain Long.
	//             Hospital data lives in hospital-service (hospital_db), and a foreign key
	//             cannot cross two different databases.
	@Column(name = "hospital_id", nullable = false)
	private Long hospitalId;
}