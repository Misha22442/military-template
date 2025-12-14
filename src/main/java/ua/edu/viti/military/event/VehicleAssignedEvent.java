package ua.edu.viti.military.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import ua.edu.viti.military.entity.Driver;
import ua.edu.viti.military.entity.Vehicle;

import java.time.LocalDateTime;

/**
 * Event published when a vehicle is assigned to a driver.
 */
@Getter
public class VehicleAssignedEvent extends ApplicationEvent {
    
    private final Long vehicleId;
    private final String vehicleRegistrationNumber;
    private final Long driverId;
    private final String driverName;
    private final LocalDateTime assignmentDate;
    
    public VehicleAssignedEvent(Object source, Vehicle vehicle, Driver driver) {
        super(source);
        this.vehicleId = vehicle.getId();
        this.vehicleRegistrationNumber = vehicle.getRegistrationNumber();
        this.driverId = driver.getId();
        this.driverName = driver.getFirstName() + " " + driver.getLastName();
        this.assignmentDate = LocalDateTime.now();
    }
}
