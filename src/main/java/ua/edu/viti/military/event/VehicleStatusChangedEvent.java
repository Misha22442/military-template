package ua.edu.viti.military.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import ua.edu.viti.military.entity.Vehicle;

import java.time.LocalDateTime;

/**
 * Event published when a vehicle status changes.
 */
@Getter
public class VehicleStatusChangedEvent extends ApplicationEvent {
    
    private final Long vehicleId;
    private final String vehicleRegistrationNumber;
    private final String oldStatus;
    private final String newStatus;
    private final LocalDateTime changedAt;
    
    public VehicleStatusChangedEvent(Object source, Vehicle vehicle, String oldStatus, String newStatus) {
        super(source);
        this.vehicleId = vehicle.getId();
        this.vehicleRegistrationNumber = vehicle.getRegistrationNumber();
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.changedAt = LocalDateTime.now();
    }
}
