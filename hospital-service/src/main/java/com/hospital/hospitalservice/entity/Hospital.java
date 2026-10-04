package com.hospital.hospitalservice.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// [MS-CHANGE] Hospital now lives in its own project (hospital-service) with its own database (hospital_db).
// [MS-CHANGE] Other services (doctor, appointment) will store only 'hospitalId' (a plain Long).
//             They will NOT have a JPA relationship (@ManyToOne) or a foreign key to this table,
//             because a foreign key cannot cross two different databases.
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hospital {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable =false,length = 100)
	private String name;
	
	@Column(length = 250)
	private String address;
	
	@Column(length = 15)
	private String phoneNumber;
	
	private LocalTime openingTime;
	private LocalTime closingTime;
	
	
}
