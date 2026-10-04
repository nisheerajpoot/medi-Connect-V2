package com.hospital.doctorservice.dto.request;

import jakarta.validation.constraints.Min;
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
public class UpdateDoctorRequestDTO {
	private String name;

	@Size(max = 100, message = "Specialization must not exceed 100 characters")
	private String specialization;

	@Min(value = 0, message = "Experience cannot be negative")
	private Integer experience;

	@Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
	private String phoneNumber;

	private Long hospitalId;
}