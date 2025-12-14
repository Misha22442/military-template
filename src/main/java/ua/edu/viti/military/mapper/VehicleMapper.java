package ua.edu.viti.military.mapper;

import org.mapstruct.*;
import ua.edu.viti.military.dto.VehicleCreateDto;
import ua.edu.viti.military.dto.VehicleResponseDto;
import ua.edu.viti.military.dto.VehicleUpdateDto;
import ua.edu.viti.military.entity.Vehicle;
import ua.edu.viti.military.entity.VehicleCategory;

/**
 * MapStruct mapper for Vehicle entity with custom maintenance logic.
 */
@Mapper(componentModel = "spring", uses = {VehicleCategoryMapper.class})
public interface VehicleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "driver", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "mileage", defaultValue = "0")
    @Mapping(target = "maintenanceIntervalKm", defaultValue = "10000")
    @Mapping(target = "lastMaintenanceMileage", defaultValue = "0")
    @Mapping(target = "status", constant = "OPERATIONAL")
    Vehicle toEntity(VehicleCreateDto dto, VehicleCategory category);

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "driverId", source = "driver.id")
    @Mapping(target = "driverFullName", expression = "java(getDriverFullName(entity))")
    @Mapping(target = "kmUntilMaintenance", expression = "java(getKilometersUntilMaintenance(entity))")
    @Mapping(target = "maintenanceStatus", expression = "java(getMaintenanceStatus(entity))")
    VehicleResponseDto toResponseDto(Vehicle entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "driver", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(@MappingTarget Vehicle entity, VehicleUpdateDto dto);

    default String getDriverFullName(Vehicle entity) {
        if (entity.getDriver() != null) {
            return entity.getDriver().getFirstName() + " " + entity.getDriver().getLastName();
        }
        return null;
    }

    default int getKilometersUntilMaintenance(Vehicle entity) {
        if (entity.getMileage() == null || entity.getLastMaintenanceMileage() == null 
                || entity.getMaintenanceIntervalKm() == null) {
            return 0;
        }
        int kmSinceLastMaintenance = entity.getMileage() - entity.getLastMaintenanceMileage();
        return entity.getMaintenanceIntervalKm() - kmSinceLastMaintenance;
    }

    default boolean isMaintenanceOverdue(Vehicle entity) {
        return getKilometersUntilMaintenance(entity) < 0;
    }

    default boolean isMaintenanceApproaching(Vehicle entity) {
        int kmUntil = getKilometersUntilMaintenance(entity);
        return kmUntil >= 0 && kmUntil <= 1000;
    }

    default String getMaintenanceStatus(Vehicle entity) {
        if (isMaintenanceOverdue(entity)) {
            return "ПРОСТРОЧЕНО! Потрібне негайне ТО";
        } else if (isMaintenanceApproaching(entity)) {
            return String.format("Наближається ТО через %d км", getKilometersUntilMaintenance(entity));
        } else {
            return String.format("OK. До ТО залишилось %d км", getKilometersUntilMaintenance(entity));
        }
    }
}
