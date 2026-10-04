package com.hospital.hospitalservice.dto.request;

import java.time.LocalTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class HospitalRequestDTO {
	@NotBlank(message = "Hospital name is required")
	@Size(max = 100, message = "Hospital name must not exceed 100 characters")
	private String name;

	@NotBlank(message = "Address is required")
	@Size(max = 250, message = "Address must not exceed 250 characters")
	private String address;

	@NotBlank(message = "Phone number is required")
	@Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
	private String phoneNumber;
	
	@NotNull(message = "Opening time is required")
	private LocalTime openingTime;

	@NotNull(message = "Closing time is required")
	private LocalTime closingTime;
}
