package ua.edu.viti.military.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Async event listener for vehicle operations.
 * Handles logging, notifications, and analytics.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class VehicleEventListener {
    
    /**
     * Handle vehicle assigned events asynchronously.
     * Could send notifications, update statistics, etc.
     */
    @EventListener
    @Async
    public void handleVehicleAssigned(VehicleAssignedEvent event) {
        log.info("🚗 ASYNC EVENT: Vehicle {} assigned to driver {} at {}",
                event.getVehicleRegistrationNumber(),
                event.getDriverName(),
                event.getAssignmentDate());
        
        // Simulate async processing (notification, logging, analytics)
        try {
            Thread.sleep(100); // Simulate some work
            log.info("   ✓ Assignment notification sent successfully");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Error processing vehicle assigned event", e);
        }
    }
    
    /**
     * Handle vehicle maintenance events asynchronously.
     * Could schedule maintenance, notify mechanics, etc.
     */
    @EventListener
    @Async
    public void handleVehicleMaintenance(VehicleMaintenanceEvent event) {
        log.info("🔧 ASYNC EVENT: Vehicle {} sent to maintenance (type: {}) at {} with mileage: {}km",
                event.getVehicleRegistrationNumber(),
                event.getMaintenanceType(),
                event.getMaintenanceDate(),
                event.getCurrentMileage());
        
        // Simulate async processing
        try {
            Thread.sleep(150); // Simulate some work
            log.info("   ✓ Maintenance record created and mechanics notified");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Error processing vehicle maintenance event", e);
        }
    }
    
    /**
     * Handle vehicle status changed events asynchronously.
     * Could update dashboards, send alerts, etc.
     */
    @EventListener
    @Async
    public void handleVehicleStatusChanged(VehicleStatusChangedEvent event) {
        log.info("📊 ASYNC EVENT: Vehicle {} status changed from {} to {} at {}",
                event.getVehicleRegistrationNumber(),
                event.getOldStatus(),
                event.getNewStatus(),
                event.getChangedAt());
        
        // Simulate async processing
        try {
            Thread.sleep(80); // Simulate some work
            log.info("   ✓ Dashboard updated with new vehicle status");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Error processing vehicle status changed event", e);
        }
    }
}
