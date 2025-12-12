package ua.edu.viti.military.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a new vehicle assignment.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleAssignmentCreateDto {

    @NotNull(message = "ID транспорту є обов'язковим")
    private Long vehicleId;

    @NotNull(message = "ID водія є обов'язковим")
    private Long driverId;

    @Size(max = 500, message = "Мета призначення не може перевищувати 500 символів")
    private String purpose;
}
