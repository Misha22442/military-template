package ua.edu.viti.military.mapper;

import org.mapstruct.*;
import ua.edu.viti.military.dto.VehicleAssignmentCreateDto;
import ua.edu.viti.military.dto.VehicleAssignmentResponseDto;
import ua.edu.viti.military.entity.Driver;
import ua.edu.viti.military.entity.Vehicle;
import ua.edu.viti.military.entity.VehicleAssignment;


/**
 * MapStruct mapper for VehicleAssignment entity.
 */
@Mapper(componentModel = "spring")
public interface VehicleAssignmentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vehicle", source = "vehicle")
    @Mapping(target = "driver", source = "driver")
    @Mapping(target = "assignmentDate", ignore = true)
    @Mapping(target = "endDate", ignore = true)
    @Mapping(target = "startMileage", ignore = true)
    @Mapping(target = "endMileage", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    VehicleAssignment toEntity(VehicleAssignmentCreateDto dto, Vehicle vehicle, Driver driver);

    @Mapping(target = "vehicleId", source = "vehicle.id")
    @Mapping(target = "vehicleModel", source = "vehicle.model")
    @Mapping(target = "vehicleRegistrationNumber", source = "vehicle.registrationNumber")
    @Mapping(target = "driverId", source = "driver.id")
    @Mapping(target = "driverFullName", expression = "java(getDriverFullName(entity))")
    @Mapping(target = "driverMilitaryRank", source = "driver.rank")
    @Mapping(target = "distanceCovered", expression = "java(calculateDistanceCovered(entity))")
    VehicleAssignmentResponseDto toResponseDto(VehicleAssignment entity);

    default String getDriverFullName(VehicleAssignment entity) {
        if (entity.getDriver() != null) {
            return entity.getDriver().getFirstName() + " " + entity.getDriver().getLastName();
        }
        return null;
    }

    default Integer calculateDistanceCovered(VehicleAssignment entity) {
        if (entity.getStartMileage() != null && entity.getEndMileage() != null) {
            return entity.getEndMileage() - entity.getStartMileage();
        }
        return null;
    }
}
