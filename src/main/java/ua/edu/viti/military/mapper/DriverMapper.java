package ua.edu.viti.military.mapper;

import org.mapstruct.*;
import ua.edu.viti.military.dto.request.DriverCreateDTO;
import ua.edu.viti.military.dto.request.DriverUpdateDTO;
import ua.edu.viti.military.dto.response.DriverResponseDTO;
import ua.edu.viti.military.entity.Driver;

/**
 * MapStruct mapper for Driver entity.
 */
@Mapper(componentModel = "spring")
public interface DriverMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isActive", defaultValue = "true")
    Driver toEntity(DriverCreateDTO dto);

    DriverResponseDTO toResponseDto(Driver entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "militaryId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(@MappingTarget Driver entity, DriverUpdateDTO dto);
}
