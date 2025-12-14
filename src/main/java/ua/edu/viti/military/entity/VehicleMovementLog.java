package ua.edu.viti.military.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity representing vehicle movement log for tracking all vehicle operations.
 * Implements audit trail for vehicle lifecycle management.
 */
@Entity
@Table(name = "vehicle_movement_logs")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleMovementLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private Driver driver;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private MovementType type;
    
    @Column(length = 500)
    private String description;
    
    @Column(length = 200)
    private String destination;
    
    @Column(name = "mileage_at_operation")
    private Integer mileageAtOperation;
    
    @Column(name = "fuel_level", precision = 10, scale = 2)
    private BigDecimal fuelLevel;
    
    @Column(name = "maintenance_type", length = 100)
    private String maintenanceType;
    
    @Column(length = 500)
    private String notes;
    
    @Column(name = "performed_by", length = 100)
    private String performedBy;
    
    @CreatedDate
    @Column(name = "performed_at", nullable = false, updatable = false)
    private LocalDateTime performedAt;
    
    /**
     * Optimistic locking version field.
     * Prevents concurrent modification conflicts.
     */
    @Version
    private Long version;
}
