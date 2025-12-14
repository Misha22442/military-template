package ua.edu.viti.military.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO for creating vehicle movement log entry.
 */
@Data
public class VehicleMovementRequestDto {
    
    @NotNull(message = "ID транспорту обов'язковий")
    @Positive
    private Long vehicleId;
    
    private Long driverId;
    
    @Size(max = 500)
    private String description;
    
    @Size(max = 200)
    private String destination;
    
    private Integer mileageAtOperation;
    
    private BigDecimal fuelLevel;
    
    @Size(max = 100)
    private String maintenanceType;
    
    @Size(max = 500)
    private String notes;
}
