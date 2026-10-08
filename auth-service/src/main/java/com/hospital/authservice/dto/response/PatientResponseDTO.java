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
public class PatientResponseDTO {
	private Long id;
	private String name;
	private Integer age;
	private String phoneNumber;
	private String address;
}