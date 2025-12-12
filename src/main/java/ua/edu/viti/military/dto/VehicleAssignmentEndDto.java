package ua.edu.viti.military.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for ending a vehicle assignment.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleAssignmentEndDto {

    @Min(value = 0, message = "Кінцевий пробіг не може бути від'ємним")
    private Integer endMileage;
}
