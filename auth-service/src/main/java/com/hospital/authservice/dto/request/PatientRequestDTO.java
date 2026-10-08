package com.hospital.authservice.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
public class PatientRequestDTO {
	@NotBlank(message = "Patient name is required")
	@Size(max = 100, message = "Patient name must not exceed 100 characters")
	private String name;

	@NotNull(message = "Age is required")
	@Min(value = 0, message = "Age cannot be negative")
	@Max(value = 120)
	private Integer age;

	@NotBlank(message = "Phone number is required")
	@Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
	private String phoneNumber;

	@NotBlank(message = "Address is required")
	@Size(max = 250, message = "Address must not exceed 250 characters")
	private String address;
}