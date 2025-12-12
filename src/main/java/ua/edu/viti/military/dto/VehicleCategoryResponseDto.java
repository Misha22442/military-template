package ua.edu.viti.military.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for vehicle category response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleCategoryResponseDto {

    private Long id;
    private String name;
    private String description;
    private String requiredLicenseCategory;
    private Integer vehicleCount;
}
