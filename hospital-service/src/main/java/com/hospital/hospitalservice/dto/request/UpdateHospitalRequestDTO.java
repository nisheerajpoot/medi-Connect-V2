package com.hospital.hospitalservice.dto.request;

import java.time.LocalTime;

import jakarta.validation.constraints.Pattern;
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
public class UpdateHospitalRequestDTO {
	private String name;
	private String address;
	
	@Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
	private String phoneNumber;
	private LocalTime openingTime;
	private LocalTime closingTime;
	
}
