package ua.edu.viti.military.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for vehicle assignment response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleAssignmentResponseDto {

    private Long id;
    
    // Vehicle info
    private Long vehicleId;
    private String vehicleModel;
    private String vehicleRegistrationNumber;
    
    // Driver info
    private Long driverId;
    private String driverFullName;
    private String driverMilitaryRank;
    
    private LocalDateTime assignmentDate;
    private LocalDateTime endDate;
    private String purpose;
    private Integer startMileage;
    private Integer endMileage;
    private Integer distanceCovered;
    private Boolean isActive;
}
