package ua.edu.viti.military.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.edu.viti.military.entity.FuelType;
import ua.edu.viti.military.entity.VehicleStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for vehicle response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleResponseDto {

    private Long id;
    private String model;
    private String registrationNumber;
    private Integer mileage;
    private String engineNumber;
    private String chassisNumber;
    private FuelType fuelType;
    private Double fuelConsumption;
    private Integer maintenanceIntervalKm;
    private LocalDate lastMaintenanceDate;
    private Integer lastMaintenanceMileage;
    private Integer manufactureYear;
    private VehicleStatus status;
    
    // Category info
    private Long categoryId;
    private String categoryName;
    
    // Driver info
    private Long driverId;
    private String driverFullName;
    
    // Maintenance status
    private Integer kmUntilMaintenance;
    private String maintenanceStatus;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
