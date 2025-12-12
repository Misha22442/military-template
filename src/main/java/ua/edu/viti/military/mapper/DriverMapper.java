package ua.edu.viti.military.mapper;

import org.springframework.stereotype.Component;
import ua.edu.viti.military.dto.request.DriverCreateDTO;
import ua.edu.viti.military.dto.request.DriverUpdateDTO;
import ua.edu.viti.military.dto.response.DriverResponseDTO;
import ua.edu.viti.military.entity.Driver;

/**
 * Mapper for Driver entity.
 */
@Component
public class DriverMapper {

    public Driver toEntity(DriverCreateDTO dto) {
        return Driver.builder()
                .militaryId(dto.getMilitaryId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .middleName(dto.getMiddleName())
                .rank(dto.getRank())
                .licenseNumber(dto.getLicenseNumber())
                .licenseCategories(dto.getLicenseCategories())
                .licenseExpiryDate(dto.getLicenseExpiryDate())
                .phoneNumber(dto.getPhoneNumber())
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
                .build();
    }

    public DriverResponseDTO toResponseDto(Driver entity) {
        DriverResponseDTO response = new DriverResponseDTO();
        response.setId(entity.getId());
        response.setMilitaryId(entity.getMilitaryId());
        response.setFirstName(entity.getFirstName());
        response.setLastName(entity.getLastName());
        response.setMiddleName(entity.getMiddleName());
        response.setRank(entity.getRank());
        response.setLicenseNumber(entity.getLicenseNumber());
        response.setLicenseCategories(entity.getLicenseCategories());
        response.setLicenseExpiryDate(entity.getLicenseExpiryDate());
        response.setPhoneNumber(entity.getPhoneNumber());
        response.setIsActive(entity.getIsActive());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        return response;
    }

    public void updateEntity(Driver entity, DriverUpdateDTO dto) {
        if (dto.getFirstName() != null) {
            entity.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            entity.setLastName(dto.getLastName());
        }
        if (dto.getMiddleName() != null) {
            entity.setMiddleName(dto.getMiddleName());
        }
        if (dto.getRank() != null) {
            entity.setRank(dto.getRank());
        }
        if (dto.getLicenseNumber() != null) {
            entity.setLicenseNumber(dto.getLicenseNumber());
        }
        if (dto.getLicenseCategories() != null) {
            entity.setLicenseCategories(dto.getLicenseCategories());
        }
        if (dto.getLicenseExpiryDate() != null) {
            entity.setLicenseExpiryDate(dto.getLicenseExpiryDate());
        }
        if (dto.getPhoneNumber() != null) {
            entity.setPhoneNumber(dto.getPhoneNumber());
        }
        if (dto.getIsActive() != null) {
            entity.setIsActive(dto.getIsActive());
        }
    }
}
