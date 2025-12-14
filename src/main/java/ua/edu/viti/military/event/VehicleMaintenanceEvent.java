package ua.edu.viti.military.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import ua.edu.viti.military.entity.Vehicle;

import java.time.LocalDateTime;

/**
 * Event published when a vehicle is sent to maintenance.
 */
@Getter
public class VehicleMaintenanceEvent extends ApplicationEvent {
    
    private final Long vehicleId;
    private final String vehicleRegistrationNumber;
    private final String maintenanceType;
    private final LocalDateTime maintenanceDate;
    private final Integer currentMileage;
    
    public VehicleMaintenanceEvent(Object source, Vehicle vehicle, String maintenanceType) {
        super(source);
        this.vehicleId = vehicle.getId();
        this.vehicleRegistrationNumber = vehicle.getRegistrationNumber();
        this.maintenanceType = maintenanceType;
        this.maintenanceDate = LocalDateTime.now();
        this.currentMileage = vehicle.getMileage();
    }
}
