package com.hospital.authservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorResponseDTO {

	private Long id;
	private String name;
	private String specialization;
	private Integer experience;
	private String phoneNumber;

	// [MS-CHANGE] Returns only hospitalId (was a nested HospitalResponseDTO in the monolith).
	//             Hospital details now live in hospital-service.
	private Long hospitalId;
}
