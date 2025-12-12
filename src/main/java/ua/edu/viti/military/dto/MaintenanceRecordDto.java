package ua.edu.viti.military.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for recording vehicle maintenance.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO для фіксації технічного обслуговування")
public class MaintenanceRecordDto {

    @Schema(description = "Дата проведення ТО", example = "2024-12-01")
    private LocalDate maintenanceDate;

    @NotNull(message = "Пробіг на момент ТО є обов'язковим")
    @Min(value = 0, message = "Пробіг не може бути від'ємним")
    @Schema(description = "Пробіг на момент ТО в км", example = "85000", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer mileage;
}
