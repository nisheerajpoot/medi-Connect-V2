package com.hospital.hospitalservice.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hospital.hospitalservice.dto.request.HospitalRequestDTO;
import com.hospital.hospitalservice.dto.request.UpdateHospitalRequestDTO;
import com.hospital.hospitalservice.dto.response.HospitalResponseDTO;
import com.hospital.hospitalservice.entity.Hospital;
import com.hospital.hospitalservice.exception.DuplicateResourceException;
import com.hospital.hospitalservice.exception.ResourceNotFoundException;
import com.hospital.hospitalservice.repository.HospitalRepository;
import com.hospital.hospitalservice.service.HospitalService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
@Transactional
public class HospitalServiceImpl implements HospitalService {
	
	   private final HospitalRepository hospitalRepository;
	

	@Override
	public HospitalResponseDTO createHospital(HospitalRequestDTO requestDTO) {
		
	    if (hospitalRepository.existsByPhoneNumber(requestDTO.getPhoneNumber())) {
	        throw new DuplicateResourceException("Hospital", "phoneNumber", requestDTO.getPhoneNumber());
	    }
      Hospital hospital=Hospital.builder()
    		  .name(requestDTO.getName())
    		  .address(requestDTO.getAddress())
    		  .phoneNumber(requestDTO.getPhoneNumber())
    		  .openingTime(requestDTO.getOpeningTime())
    		  .closingTime(requestDTO.getClosingTime())
    		  .build();
      
      Hospital savedHospital=hospitalRepository.save(hospital);
      return mapToResponseDTO(savedHospital);
        
	}

	@Override
	public HospitalResponseDTO getHospitalById(Long id) {
		// TODO Auto-generated method stub
		Hospital hospital = hospitalRepository.findById(id)
		        .orElseThrow(() -> new ResourceNotFoundException("Hospital", "id", id));
		return mapToResponseDTO(hospital);
	}

	@Override
	public List<HospitalResponseDTO> getAllHospitals() {
		// TODO Auto-generated method stub
		 List<Hospital> hospitals = hospitalRepository.findAll();
	        List<HospitalResponseDTO> responseList = new ArrayList<>();
	        for (Hospital hospital : hospitals) {
	            responseList.add(mapToResponseDTO(hospital));
	        }
	        return responseList;
	}

	@Override
	public HospitalResponseDTO updateHospital(Long id, UpdateHospitalRequestDTO requestDTO) {
		Hospital hospital =hospitalRepository.findById(id)
				.orElseThrow(()-> new ResourceNotFoundException("Hospital","id",id));
		if (requestDTO.getName() == null &&
			requestDTO.getAddress() == null &&
			requestDTO.getPhoneNumber() == null &&
			requestDTO.getOpeningTime() == null &&
			requestDTO.getClosingTime() == null) {

		        throw new IllegalArgumentException("At least one field must be provided for update");
		    }
		
		  if (requestDTO.getName() != null) {
		        if (requestDTO.getName().isBlank()) {
		            throw new IllegalArgumentException("Name cannot be blank");
		        }
		        hospital.setName(requestDTO.getName() .trim());
		    }
		  
		  if (requestDTO.getAddress() != null) {
		        if (requestDTO.getAddress() .isBlank()) {
		            throw new IllegalArgumentException("Address cannot be blank");
		        }
		        hospital.setAddress(requestDTO.getAddress()); 
		    }
		  
		  if (requestDTO.getPhoneNumber() != null) {
			    if (requestDTO.getPhoneNumber().isBlank()) {
			        throw new IllegalArgumentException("Phone cannot be blank");
			    }

			    String newPhoneNumber = requestDTO.getPhoneNumber().trim();

			    if (!newPhoneNumber.equals(hospital.getPhoneNumber()) &&
			            hospitalRepository.existsByPhoneNumber(newPhoneNumber)) {
			        throw new DuplicateResourceException("Hospital", "phoneNumber", newPhoneNumber);
			    }

			    hospital.setPhoneNumber(newPhoneNumber);
			}
		  
		  if (requestDTO.getOpeningTime() != null) {
			    hospital.setOpeningTime(requestDTO.getOpeningTime());
			}

		  if (requestDTO.getClosingTime() != null) {
			    hospital.setClosingTime(requestDTO.getClosingTime());
		    }
		 

		return mapToResponseDTO(hospital);
	}

	@Override
	// [MS-CHANGE / TODO Phase 2] In the monolith, deleting a hospital that still had doctors failed because of the
	//             database foreign key. In microservices there is no foreign key, so a delete would silently leave
	//             doctors pointing to a hospital that no longer exists (orphan data).
	//             Fix later: ask doctor-service (via Feign) whether this hospital still has doctors, then block the delete.
	public void deleteHospital(Long id) {
		Hospital hospital =hospitalRepository.findById(id)
				.orElseThrow(()-> new ResourceNotFoundException("Hospital","id",id));
		hospitalRepository.delete(hospital);
	}

	@Override
	public List<HospitalResponseDTO> searchHospitalByName(String name) {
		 List<Hospital> hospitals=hospitalRepository.findByNameContainingIgnoreCase(name);
		 List<HospitalResponseDTO> responseList = new ArrayList<>();
	        for (Hospital hospital : hospitals) {
	            responseList.add(mapToResponseDTO(hospital));
	        }
	        return responseList;
	}
	private HospitalResponseDTO mapToResponseDTO(Hospital hospital) {
	    return HospitalResponseDTO.builder()
	            .id(hospital.getId())
	            .name(hospital.getName())
	            .address(hospital.getAddress())
	            .phoneNumber(hospital.getPhoneNumber())
	            .openingTime(hospital.getOpeningTime())
	            .closingTime(hospital.getClosingTime())
	            .build();
	}

	

}
