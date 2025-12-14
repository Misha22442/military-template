package ua.edu.viti.military.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.edu.viti.military.entity.MovementType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for vehicle movement log response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleMovementResponseDto {
    
    private Long id;
    private Long vehicleId;
    private String vehicleRegistrationNumber;
    private Long driverId;
    private String driverName;
    private MovementType type;
    private String description;
    private String destination;
    private Integer mileageAtOperation;
    private BigDecimal fuelLevel;
    private String maintenanceType;
    private String notes;
    private String performedBy;
    private LocalDateTime performedAt;
}
