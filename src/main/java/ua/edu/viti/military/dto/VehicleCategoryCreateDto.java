package ua.edu.viti.military.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a new vehicle category.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleCategoryCreateDto {

    @NotBlank(message = "Назва категорії є обов'язковою")
    @Size(min = 2, max = 100, message = "Назва категорії повинна бути від 2 до 100 символів")
    private String name;

    @Size(max = 500, message = "Опис не може перевищувати 500 символів")
    private String description;

    @Size(max = 20, message = "Категорія ліцензії не може перевищувати 20 символів")
    private String requiredLicenseCategory;
}
