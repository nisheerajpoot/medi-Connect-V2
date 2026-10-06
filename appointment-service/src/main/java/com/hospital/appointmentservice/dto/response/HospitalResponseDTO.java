package com.hospital.appointmentservice.dto.response;

import java.time.LocalTime;

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
public class HospitalResponseDTO {
	private Long id;
	private String name;
	private String address;
	private String phoneNumber;
	private LocalTime openingTime;
	private LocalTime closingTime;
	
}
