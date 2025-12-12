package ua.edu.viti.military.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.edu.viti.military.entity.DriverStatus;

import java.time.LocalDate;

/**
 * DTO for updating an existing driver.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverUpdateDto {

    @Size(min = 2, max = 50, message = "Ім'я повинно бути від 2 до 50 символів")
    private String firstName;

    @Size(min = 2, max = 50, message = "Прізвище повинно бути від 2 до 50 символів")
    private String lastName;

    @Size(max = 50, message = "Військове звання не може перевищувати 50 символів")
    private String militaryRank;

    @Size(max = 20, message = "Табельний номер не може перевищувати 20 символів")
    private String serviceNumber;

    @Size(max = 50, message = "Категорії ліцензії не можуть перевищувати 50 символів")
    @Pattern(regexp = "^[A-Z,]+$", message = "Категорії ліцензії повинні бути великими літерами, розділеними комами")
    private String licenseCategories;

    private LocalDate licenseExpiryDate;

    private DriverStatus status;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Невірний формат номера телефону")
    private String phoneNumber;
}
