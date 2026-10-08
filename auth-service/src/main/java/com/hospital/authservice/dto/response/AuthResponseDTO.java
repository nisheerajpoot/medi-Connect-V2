package com.hospital.authservice.dto.response;



import com.hospital.authservice.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Frontend isko save karta hai: role se portal decide hota hai, profileId se apna data fetch hota hai.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDTO {
	private Long userId;
	private String email;
	private Role role;
	private Long profileId;
	private String name;
	private String token;
}