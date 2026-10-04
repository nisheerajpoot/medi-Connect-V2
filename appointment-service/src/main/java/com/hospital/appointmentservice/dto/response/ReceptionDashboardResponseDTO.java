package com.hospital.appointmentservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceptionDashboardResponseDTO {
	   private Long hospitalId;
	    private String hospitalName;
	    private long pendingCount;
	    private long confirmedCount;
	    private long rejectedCount;
}