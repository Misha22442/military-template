package ua.edu.viti.military.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.edu.viti.military.entity.DriverStatus;

import java.time.LocalDate;

/**
 * DTO for creating a new driver.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverCreateDto {

    @NotBlank(message = "Ім'я є обов'язковим")
    @Size(min = 2, max = 50, message = "Ім'я повинно бути від 2 до 50 символів")
    private String firstName;

    @NotBlank(message = "Прізвище є обов'язковим")
    @Size(min = 2, max = 50, message = "Прізвище повинно бути від 2 до 50 символів")
    private String lastName;

    @Size(max = 50, message = "Військове звання не може перевищувати 50 символів")
    private String militaryRank;

    @Size(max = 20, message = "Табельний номер не може перевищувати 20 символів")
    private String serviceNumber;

    @NotBlank(message = "Категорії ліцензії є обов'язковими")
    @Size(max = 50, message = "Категорії ліцензії не можуть перевищувати 50 символів")
    @Pattern(regexp = "^[A-Z,]+$", message = "Категорії ліцензії повинні бути великими літерами, розділеними комами")
    private String licenseCategories;

    @Future(message = "Дата закінчення ліцензії повинна бути в майбутньому")
    private LocalDate licenseExpiryDate;

    private DriverStatus status;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Невірний формат номера телефону")
    private String phoneNumber;
}
