package ua.edu.viti.military.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleCategoryCreateDTO {

    @NotBlank(message = "Назва категорії обов'язкова")
    @Size(max = 100, message = "Назва не може бути довшою за 100 символів")
    private String name;

    @NotBlank(message = "Код категорії обов'язковий")
    @Size(max = 20, message = "Код не може бути довшим за 20 символів")
    private String code;

    @Size(max = 500, message = "Опис не може бути довшим за 500 символів")
    private String description;

    @Size(max = 20, message = "Категорія прав не може бути довшою за 20 символів")
    private String requiredLicense;

    @Positive(message = "Вантажопідйомність має бути позитивною")
    private Integer maxLoadCapacity;
}
