package com.hospital.hospitalservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.hospitalservice.dto.request.HospitalRequestDTO;
import com.hospital.hospitalservice.dto.request.UpdateHospitalRequestDTO;
import com.hospital.hospitalservice.dto.response.ApiResponseDTO;
import com.hospital.hospitalservice.dto.response.HospitalResponseDTO;
import com.hospital.hospitalservice.service.HospitalService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

// [MS-CHANGE] Same URL (/api/hospitals) as the monolith, so the frontend does not need to change.
// [MS-CHANGE] Runs on its own port (8081). Later the API Gateway will route /api/hospitals/** to this service.
@RestController
@RequestMapping("/api/hospitals")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HospitalController {
	private final HospitalService hospitalService;
	
	@PostMapping
	public ResponseEntity<ApiResponseDTO<HospitalResponseDTO>> createHospital(
			@Valid @RequestBody HospitalRequestDTO requestDTO) {

		HospitalResponseDTO response = hospitalService.createHospital(requestDTO);

		ApiResponseDTO<HospitalResponseDTO> apiResponse = ApiResponseDTO.success("Hospital created successfully", response);

		return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
	}
	
	 // [MS-CHANGE] doctor-service and appointment-service will call this endpoint (using Feign, Phase 2)
	 //             to check that a hospitalId really exists before saving a doctor or an appointment.
	 @GetMapping("/{id}")
	    public ResponseEntity<ApiResponseDTO<HospitalResponseDTO>> getHospitalById(@PathVariable Long id) {
		 HospitalResponseDTO hospital = hospitalService.getHospitalById(id);
	        return ResponseEntity.ok(ApiResponseDTO.success(hospital));
	   }
	 
	 
	 @GetMapping
	    public ResponseEntity<ApiResponseDTO<List<HospitalResponseDTO>>> getAllHospitals() {
	        List<HospitalResponseDTO> hospitals = hospitalService.getAllHospitals();
	        return ResponseEntity.ok(ApiResponseDTO.success("Fetched " + hospitals.size() + " hospitals", hospitals));
	 }
	 
	 @PutMapping("/{id}")
	 public ResponseEntity<ApiResponseDTO<HospitalResponseDTO>> updateHospital(
	         @PathVariable Long id,
	         @Valid @RequestBody UpdateHospitalRequestDTO requestDTO) {
		 HospitalResponseDTO updatedHospital =
				 hospitalService.updateHospital(id, requestDTO);

	        return ResponseEntity.ok(ApiResponseDTO.success("Hospital updated successfully",updatedHospital));
	 }
	 
	 @DeleteMapping("/{id}")
	 public ResponseEntity<ApiResponseDTO<Void>> deleteHospital(@PathVariable Long id) {
		 hospitalService.deleteHospital(id);
	        return ResponseEntity.ok(ApiResponseDTO.success("Hospital delete successfully"));
	 }
	 
	 @GetMapping("/search")
	 public ResponseEntity<ApiResponseDTO<List<HospitalResponseDTO>>> searchHospitalByName(@RequestParam String name) {
		 List<HospitalResponseDTO> hospitals = hospitalService.searchHospitalByName(name);
		 return ResponseEntity.ok(ApiResponseDTO.success(hospitals));
	 }

}








