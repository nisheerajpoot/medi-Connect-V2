package com.hospital.authservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterDoctorRequest extends RegisterBaseRequest {
	@NotBlank(message = "Doctor name is required")
	@Size(max = 100, message = "Doctor name must not exceed 100 characters")
	private String name;

	@NotBlank(message = "Specialization is required")
	@Size(max = 100, message = "Specialization must not exceed 100 characters")
	private String specialization;

	@NotNull(message = "Experience is required")
	@Min(value = 0, message = "Experience cannot be negative")
	private Integer experience;

	@NotBlank(message = "Phone number is required")
	@Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
	private String phoneNumber;

	@NotNull(message = "Hospital is required")
	private Long hospitalId;
}