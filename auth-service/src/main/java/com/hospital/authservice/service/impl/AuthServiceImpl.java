package com.hospital.authservice.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hospital.authservice.client.DoctorClient;
import com.hospital.authservice.client.HospitalClient;
import com.hospital.authservice.client.PatientClient;
import com.hospital.authservice.dto.request.DoctorRequestDTO;
import com.hospital.authservice.dto.request.HospitalRequestDTO;
import com.hospital.authservice.dto.request.LoginRequest;
import com.hospital.authservice.dto.request.PatientRequestDTO;
import com.hospital.authservice.dto.request.RegisterDoctorRequest;
import com.hospital.authservice.dto.request.RegisterHospitalRequest;
import com.hospital.authservice.dto.request.RegisterPatientRequest;
import com.hospital.authservice.dto.response.AuthResponseDTO;
import com.hospital.authservice.dto.response.DoctorResponseDTO;
import com.hospital.authservice.dto.response.HospitalResponseDTO;
import com.hospital.authservice.dto.response.PatientResponseDTO;
import com.hospital.authservice.entity.Role;
import com.hospital.authservice.entity.User;
import com.hospital.authservice.exception.DuplicateResourceException;
import com.hospital.authservice.exception.ResourceNotFoundException;
import com.hospital.authservice.exception.UnauthorizedException;
import com.hospital.authservice.repository.UserRepository;
import com.hospital.authservice.service.AuthService;
import com.hospital.authservice.service.JwtService;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

// [MS-CHANGE] No @Transactional on this class. The profile lives in another service (another database),
//             so one database transaction can no longer cover "create profile + create user".
//             Instead we use COMPENSATION: if saving the user fails, we undo the profile (see registerXxx).
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	
	// [MS-CHANGE] The monolith called PatientService/DoctorService/HospitalService directly.
	//             Now we call the other services over HTTP with Feign clients (found by name through Eureka).
	private final PatientClient patientClient;
	private final DoctorClient doctorClient;
	private final HospitalClient hospitalClient;

	// ---------- register ----------

	@Override
	public AuthResponseDTO registerPatient(RegisterPatientRequest request) {
		String email = checkEmailFree(request.getEmail());
		PatientResponseDTO patient = createPatientProfile(request);

		try {
			return saveUser(email, request.getPassword(), Role.PATIENT, patient.getId(), patient.getName());
		} catch (RuntimeException e) {
			undo("patient", patient.getId(), () -> patientClient.deletePatient(patient.getId()));
			throw e;
		}
	}

	@Override
	public AuthResponseDTO registerDoctor(RegisterDoctorRequest request) {
		String email = checkEmailFree(request.getEmail());
		DoctorResponseDTO doctor = createDoctorProfile(request);

		try {
			return saveUser(email, request.getPassword(), Role.DOCTOR, doctor.getId(), doctor.getName());
		} catch (RuntimeException e) {
			undo("doctor", doctor.getId(), () -> doctorClient.deleteDoctor(doctor.getId()));
			throw e;
		}
	}

	@Override
	public AuthResponseDTO registerHospital(RegisterHospitalRequest request) {
		String email = checkEmailFree(request.getEmail());
		HospitalResponseDTO hospital = createHospitalProfile(request);

		try {
			return saveUser(email, request.getPassword(), Role.HOSPITAL, hospital.getId(), hospital.getName());
		} catch (RuntimeException e) {
			undo("hospital", hospital.getId(), () -> hospitalClient.deleteHospital(hospital.getId()));
			throw e;
		}
	}

	// ---------- login ----------

	@Override
	public AuthResponseDTO login(LoginRequest request) {
		// wrong email or wrong password: same message, so an attacker gets no hint
		User user = userRepository.findByEmail(normalize(request.getEmail()))
				.orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new UnauthorizedException("Invalid email or password");
		}

		return toResponse(user, resolveName(user));
	}

	// ---------- profile creation through Feign ----------

	private PatientResponseDTO createPatientProfile(RegisterPatientRequest r) {
		try {
			return patientClient.createPatient(PatientRequestDTO.builder()
					.name(r.getName())
					.age(r.getAge())
					.phoneNumber(r.getPhoneNumber())
					.address(r.getAddress())
					.build()).getData();
		} catch (FeignException.Conflict e) {
			throw new DuplicateResourceException("Patient", "phoneNumber", r.getPhoneNumber());
		}
	}

	private DoctorResponseDTO createDoctorProfile(RegisterDoctorRequest r) {
		try {
			return doctorClient.createDoctor(DoctorRequestDTO.builder()
					.name(r.getName())
					.specialization(r.getSpecialization())
					.experience(r.getExperience())
					.phoneNumber(r.getPhoneNumber())
					.hospitalId(r.getHospitalId())
					.build()).getData();
		} catch (FeignException.Conflict e) {
			throw new DuplicateResourceException("Doctor", "phoneNumber", r.getPhoneNumber());
		} catch (FeignException.NotFound e) {
			// doctor-service answers 404 when the hospital does not exist
			throw new ResourceNotFoundException("Hospital", "id", r.getHospitalId());
		}
	}

	private HospitalResponseDTO createHospitalProfile(RegisterHospitalRequest r) {
		try {
			return hospitalClient.createHospital(HospitalRequestDTO.builder()
					.name(r.getName())
					.address(r.getAddress())
					.phoneNumber(r.getPhoneNumber())
					.openingTime(r.getOpeningTime())
					.closingTime(r.getClosingTime())
					.build()).getData();
		} catch (FeignException.Conflict e) {
			throw new DuplicateResourceException("Hospital", "phoneNumber", r.getPhoneNumber());
		}
	}

	// ---------- helpers ----------

	private String normalize(String email) {
		return email == null ? "" : email.trim().toLowerCase();
	}

	private String checkEmailFree(String rawEmail) {
		String email = normalize(rawEmail);
		if (userRepository.existsByEmail(email)) {
			throw new DuplicateResourceException("User", "email", email);
		}
		return email;
	}

	private AuthResponseDTO saveUser(String email, String rawPassword, Role role, Long profileId, String name) {
		User user = userRepository.save(User.builder()
				.email(email)
				.password(passwordEncoder.encode(rawPassword))
				.role(role)
				.profileId(profileId)
				.build());
		return toResponse(user, name);
	}

	// Compensation: undo a profile. If even the undo fails we only log it,
	// so the original error is not hidden.
	private void undo(String what, Long id, Runnable action) {
		try {
			action.run();
			log.warn("User save failed, so the {} profile {} was removed (compensation)", what, id);
		} catch (Exception ex) {
			log.error("Could not remove the {} profile {} after a failed register. Clean it manually.", what, id, ex);
		}
	}

	private String resolveName(User user) {
		try {
			return switch (user.getRole()) {
				case PATIENT -> patientClient.getPatientById(user.getProfileId()).getData().getName();
				case DOCTOR -> doctorClient.getDoctorById(user.getProfileId()).getData().getName();
				case HOSPITAL -> hospitalClient.getHospitalById(user.getProfileId()).getData().getName();
			};
		} catch (FeignException.NotFound e) {
			throw new UnauthorizedException("The profile of this account no longer exists");
		}
	}

	private AuthResponseDTO toResponse(User user, String name) {
	    String token = jwtService.generateToken(
	            user.getId(), user.getEmail(), user.getRole().name(), user.getProfileId(), name);

	    return AuthResponseDTO.builder()
	            .userId(user.getId())
	            .email(user.getEmail())
	            .role(user.getRole())
	            .profileId(user.getProfileId())
	            .name(name)
	            .token(token)
	            .build();
	}
}