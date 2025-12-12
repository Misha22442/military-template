package ua.edu.viti.military.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.edu.viti.military.entity.FuelType;

import java.time.LocalDate;

/**
 * DTO for creating a new vehicle.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleCreateDto {

    @NotBlank(message = "Модель транспорту є обов'язковою")
    @Size(min = 2, max = 100, message = "Модель повинна бути від 2 до 100 символів")
    private String model;

    @NotBlank(message = "Реєстраційний номер є обов'язковим")
    @Size(min = 3, max = 20, message = "Реєстраційний номер повинен бути від 3 до 20 символів")
    private String registrationNumber;

    @Min(value = 0, message = "Пробіг не може бути від'ємним")
    private Integer mileage;

    @Size(max = 50, message = "Номер двигуна не може перевищувати 50 символів")
    private String engineNumber;

    @Size(max = 50, message = "Номер шасі не може перевищувати 50 символів")
    private String chassisNumber;

    @NotNull(message = "Тип палива є обов'язковим")
    private FuelType fuelType;

    @Positive(message = "Витрата палива повинна бути позитивною")
    private Double fuelConsumption;

    @Min(value = 1000, message = "Інтервал ТО повинен бути не менше 1000 км")
    private Integer maintenanceIntervalKm;

    private LocalDate lastMaintenanceDate;

    @Min(value = 0, message = "Пробіг останнього ТО не може бути від'ємним")
    private Integer lastMaintenanceMileage;

    @Min(value = 1900, message = "Рік випуску повинен бути не раніше 1900")
    @Max(value = 2100, message = "Рік випуску повинен бути не пізніше 2100")
    private Integer manufactureYear;

    @NotNull(message = "ID категорії є обов'язковим")
    private Long categoryId;
}
