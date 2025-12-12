package ua.edu.viti.military.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.edu.viti.military.entity.DriverStatus;

import java.time.LocalDate;

/**
 * DTO for driver response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverResponseDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String militaryRank;
    private String serviceNumber;
    private String licenseCategories;
    private LocalDate licenseExpiryDate;
    private DriverStatus status;
    private String statusDisplayName;
    private String phoneNumber;
    private Boolean isActive;
    private Boolean isLicenseValid;
    private Integer activeAssignmentsCount;
}
