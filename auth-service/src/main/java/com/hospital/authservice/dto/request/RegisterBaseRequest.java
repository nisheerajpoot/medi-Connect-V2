package com.hospital.authservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Teeno registrations (patient/doctor/hospital) me common fields
@Getter
@Setter
@NoArgsConstructor
public class RegisterBaseRequest {
	@NotBlank(message = "Email is required")
	@Email(message = "Enter a valid email")
	@Size(max = 100, message = "Email must not exceed 100 characters")
	private String email;

	@NotBlank(message = "Password is required")
	@Size(min = 6, max = 50, message = "Password must be 6 to 50 characters")
	private String password;
}
