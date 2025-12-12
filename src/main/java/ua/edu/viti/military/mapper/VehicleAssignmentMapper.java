package ua.edu.viti.military.mapper;

import org.springframework.stereotype.Component;
import ua.edu.viti.military.dto.VehicleAssignmentCreateDto;
import ua.edu.viti.military.dto.VehicleAssignmentResponseDto;
import ua.edu.viti.military.entity.Driver;
import ua.edu.viti.military.entity.Vehicle;
import ua.edu.viti.military.entity.VehicleAssignment;

import java.time.LocalDateTime;

/**
 * Mapper for VehicleAssignment entity.
 */
@Component
public class VehicleAssignmentMapper {

    public VehicleAssignment toEntity(VehicleAssignmentCreateDto dto, Vehicle vehicle, Driver driver) {
        return VehicleAssignment.builder()
                .vehicle(vehicle)
                .driver(driver)
                .assignmentDate(LocalDateTime.now())
                .purpose(dto.getPurpose())
                .isActive(true)
                .build();
    }

    public VehicleAssignmentResponseDto toResponseDto(VehicleAssignment entity) {
        return VehicleAssignmentResponseDto.builder()
                .id(entity.getId())
                .vehicleId(entity.getVehicle() != null ? entity.getVehicle().getId() : null)
                .vehicleModel(entity.getVehicle() != null ? entity.getVehicle().getModel() : null)
                .vehicleRegistrationNumber(entity.getVehicle() != null ? 
                        entity.getVehicle().getRegistrationNumber() : null)
                .driverId(entity.getDriver() != null ? entity.getDriver().getId() : null)
                .driverFullName(entity.getDriver() != null ? entity.getDriver().getFullName() : null)
                .driverMilitaryRank(entity.getDriver() != null ? 
                        entity.getDriver().getRank() : null)
                .assignmentDate(entity.getAssignmentDate())
                .endDate(entity.getEndDate())
                .purpose(entity.getPurpose())
                .startMileage(entity.getStartMileage())
                .endMileage(entity.getEndMileage())
                .distanceCovered(entity.getDistanceCovered())
                .isActive(entity.getIsActive())
                .build();
    }
}
