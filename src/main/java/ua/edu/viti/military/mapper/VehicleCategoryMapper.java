package ua.edu.viti.military.mapper;

import org.springframework.stereotype.Component;
import ua.edu.viti.military.dto.request.VehicleCategoryCreateDTO;
import ua.edu.viti.military.dto.response.VehicleCategoryResponseDTO;
import ua.edu.viti.military.entity.VehicleCategory;

/**
 * Mapper for VehicleCategory entity.
 */
@Component
public class VehicleCategoryMapper {

    public VehicleCategory toEntity(VehicleCategoryCreateDTO dto) {
        return VehicleCategory.builder()
                .name(dto.getName())
                .code(dto.getCode())
                .description(dto.getDescription())
                .requiredLicense(dto.getRequiredLicense())
                .maxLoadCapacity(dto.getMaxLoadCapacity())
                .build();
    }

    public VehicleCategoryResponseDTO toResponseDto(VehicleCategory entity) {
        VehicleCategoryResponseDTO response = new VehicleCategoryResponseDTO();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setCode(entity.getCode());
        response.setDescription(entity.getDescription());
        response.setRequiredLicense(entity.getRequiredLicense());
        response.setMaxLoadCapacity(entity.getMaxLoadCapacity());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        return response;
    }

    public void updateEntity(VehicleCategory entity, VehicleCategoryCreateDTO dto) {
        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }
        if (dto.getCode() != null) {
            entity.setCode(dto.getCode());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
        if (dto.getRequiredLicense() != null) {
            entity.setRequiredLicense(dto.getRequiredLicense());
        }
        if (dto.getMaxLoadCapacity() != null) {
            entity.setMaxLoadCapacity(dto.getMaxLoadCapacity());
        }
    }
}
