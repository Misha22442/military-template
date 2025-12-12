package ua.edu.viti.military.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverUpdateDTO {

    @Size(max = 50, message = "Ім'я не може бути довшим за 50 символів")
    private String firstName;

    @Size(max = 50, message = "Прізвище не може бути довшим за 50 символів")
    private String lastName;

    @Size(max = 50, message = "По батькові не може бути довшим за 50 символів")
    private String middleName;

    @Size(max = 50, message = "Звання не може бути довшим за 50 символів")
    private String rank;

    @Size(max = 50, message = "Номер посвідчення не може бути довшим за 50 символів")
    private String licenseNumber;

    @Size(max = 50, message = "Категорії прав не можуть бути довшими за 50 символів")
    private String licenseCategories;

    @Future(message = "Дата закінчення посвідчення має бути в майбутньому")
    private LocalDate licenseExpiryDate;

    @Size(max = 20, message = "Номер телефону не може бути довшим за 20 символів")
    private String phoneNumber;

    private Boolean isActive;
}
