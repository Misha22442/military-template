package ua.edu.viti.military.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.edu.viti.military.entity.FuelType;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleCreateDTO {

    @NotBlank(message = "Модель транспорту обов'язкова")
    @Size(max = 100, message = "Модель не може бути довшою за 100 символів")
    private String model;

    @NotBlank(message = "Реєстраційний номер обов'язковий")
    @Size(max = 20, message = "Реєстраційний номер не може бути довшим за 20 символів")
    private String registrationNumber;

    @NotNull(message = "Категорія транспорту обов'язкова")
    @Positive(message = "ID категорії має бути позитивним")
    private Long categoryId;

    @Size(max = 50, message = "Номер двигуна не може бути довшим за 50 символів")
    private String engineNumber;

    @Size(max = 50, message = "Номер шасі не може бути довшим за 50 символів")
    private String chassisNumber;

    @Positive(message = "Рік випуску має бути позитивним")
    private Integer manufactureYear;

    @PositiveOrZero(message = "Пробіг не може бути від'ємним")
    private Integer mileage = 0;

    @NotNull(message = "Тип палива обов'язковий")
    private FuelType fuelType;

    @Positive(message = "Витрата палива має бути позитивною")
    private Double fuelConsumption;

    @Positive(message = "Інтервал ТО має бути позитивним")
    private Integer maintenanceIntervalKm = 10000;

    private LocalDate lastMaintenanceDate;

    @PositiveOrZero(message = "Пробіг при останньому ТО не може бути від'ємним")
    private Integer lastMaintenanceMileage = 0;

    @Positive(message = "ID водія має бути позитивним")
    private Long driverId;
}
