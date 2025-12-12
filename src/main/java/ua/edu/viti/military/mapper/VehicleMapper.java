package ua.edu.viti.military.mapper;

import org.springframework.stereotype.Component;
import ua.edu.viti.military.dto.VehicleCreateDto;
import ua.edu.viti.military.dto.VehicleResponseDto;
import ua.edu.viti.military.dto.VehicleUpdateDto;
import ua.edu.viti.military.entity.Vehicle;
import ua.edu.viti.military.entity.VehicleCategory;
import ua.edu.viti.military.entity.VehicleStatus;

/**
 * Mapper for Vehicle entity.
 */
@Component
public class VehicleMapper {

    public Vehicle toEntity(VehicleCreateDto dto, VehicleCategory category) {
        Vehicle vehicle = new Vehicle();
        vehicle.setModel(dto.getModel());
        vehicle.setRegistrationNumber(dto.getRegistrationNumber());
        vehicle.setMileage(dto.getMileage() != null ? dto.getMileage() : 0);
        vehicle.setEngineNumber(dto.getEngineNumber());
        vehicle.setChassisNumber(dto.getChassisNumber());
        vehicle.setFuelType(dto.getFuelType());
        vehicle.setFuelConsumption(dto.getFuelConsumption());
        vehicle.setMaintenanceIntervalKm(dto.getMaintenanceIntervalKm() != null ? dto.getMaintenanceIntervalKm() : 10000);
        vehicle.setLastMaintenanceDate(dto.getLastMaintenanceDate());
        vehicle.setLastMaintenanceMileage(dto.getLastMaintenanceMileage() != null ? dto.getLastMaintenanceMileage() : 0);
        vehicle.setManufactureYear(dto.getManufactureYear());
        vehicle.setCategory(category);
        vehicle.setStatus(VehicleStatus.OPERATIONAL);
        return vehicle;
    }

    public VehicleResponseDto toResponseDto(Vehicle entity) {
        String maintenanceStatus = getMaintenanceStatus(entity);
        int kmUntilMaintenance = getKilometersUntilMaintenance(entity);
        
        VehicleResponseDto dto = new VehicleResponseDto();
        dto.setId(entity.getId());
        dto.setModel(entity.getModel());
        dto.setRegistrationNumber(entity.getRegistrationNumber());
        dto.setMileage(entity.getMileage());
        dto.setEngineNumber(entity.getEngineNumber());
        dto.setChassisNumber(entity.getChassisNumber());
        dto.setFuelType(entity.getFuelType());
        dto.setFuelConsumption(entity.getFuelConsumption());
        dto.setMaintenanceIntervalKm(entity.getMaintenanceIntervalKm());
        dto.setLastMaintenanceDate(entity.getLastMaintenanceDate());
        dto.setLastMaintenanceMileage(entity.getLastMaintenanceMileage());
        dto.setManufactureYear(entity.getManufactureYear());
        dto.setStatus(entity.getStatus());
        
        if (entity.getCategory() != null) {
            dto.setCategoryId(entity.getCategory().getId());
            dto.setCategoryName(entity.getCategory().getName());
        }
        
        if (entity.getDriver() != null) {
            dto.setDriverId(entity.getDriver().getId());
            dto.setDriverFullName(entity.getDriver().getFirstName() + " " + entity.getDriver().getLastName());
        }
        
        dto.setKmUntilMaintenance(kmUntilMaintenance);
        dto.setMaintenanceStatus(maintenanceStatus);
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        
        return dto;
    }

    public void updateEntity(Vehicle entity, VehicleUpdateDto dto) {
        if (dto.getModel() != null) {
            entity.setModel(dto.getModel());
        }
        if (dto.getRegistrationNumber() != null) {
            entity.setRegistrationNumber(dto.getRegistrationNumber());
        }
        if (dto.getMileage() != null) {
            entity.setMileage(dto.getMileage());
        }
        if (dto.getEngineNumber() != null) {
            entity.setEngineNumber(dto.getEngineNumber());
        }
        if (dto.getChassisNumber() != null) {
            entity.setChassisNumber(dto.getChassisNumber());
        }
        if (dto.getFuelType() != null) {
            entity.setFuelType(dto.getFuelType());
        }
        if (dto.getFuelConsumption() != null) {
            entity.setFuelConsumption(dto.getFuelConsumption());
        }
        if (dto.getMaintenanceIntervalKm() != null) {
            entity.setMaintenanceIntervalKm(dto.getMaintenanceIntervalKm());
        }
        if (dto.getLastMaintenanceDate() != null) {
            entity.setLastMaintenanceDate(dto.getLastMaintenanceDate());
        }
        if (dto.getLastMaintenanceMileage() != null) {
            entity.setLastMaintenanceMileage(dto.getLastMaintenanceMileage());
        }
        if (dto.getManufactureYear() != null) {
            entity.setManufactureYear(dto.getManufactureYear());
        }
        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }
    }

    private int getKilometersUntilMaintenance(Vehicle entity) {
        if (entity.getMileage() == null || entity.getLastMaintenanceMileage() == null 
                || entity.getMaintenanceIntervalKm() == null) {
            return 0;
        }
        int kmSinceLastMaintenance = entity.getMileage() - entity.getLastMaintenanceMileage();
        return entity.getMaintenanceIntervalKm() - kmSinceLastMaintenance;
    }

    private boolean isMaintenanceOverdue(Vehicle entity) {
        return getKilometersUntilMaintenance(entity) < 0;
    }

    private boolean isMaintenanceApproaching(Vehicle entity) {
        int kmUntil = getKilometersUntilMaintenance(entity);
        return kmUntil >= 0 && kmUntil <= 1000;
    }

    private String getMaintenanceStatus(Vehicle entity) {
        if (isMaintenanceOverdue(entity)) {
            return "ПРОСТРОЧЕНО! Потрібне негайне ТО";
        } else if (isMaintenanceApproaching(entity)) {
            return String.format("Наближається ТО через %d км", getKilometersUntilMaintenance(entity));
        } else {
            return String.format("OK. До ТО залишилось %d км", getKilometersUntilMaintenance(entity));
        }
    }
}
