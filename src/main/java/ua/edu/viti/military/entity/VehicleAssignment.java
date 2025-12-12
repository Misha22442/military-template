package ua.edu.viti.military.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entity representing assignment of a vehicle to a driver.
 */
@Entity
@Table(name = "vehicle_assignments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    @Column(name = "assignment_date", nullable = false)
    private LocalDateTime assignmentDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(length = 500)
    private String purpose;

    @Column(name = "start_mileage")
    private Integer startMileage;

    @Column(name = "end_mileage")
    private Integer endMileage;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    /**
     * Ends the assignment and records final mileage
     */
    public void endAssignment(Integer finalMileage) {
        this.endDate = LocalDateTime.now();
        this.endMileage = finalMileage;
        this.isActive = false;
    }

    /**
     * Calculates distance covered during assignment
     */
    public Integer getDistanceCovered() {
        if (startMileage == null || endMileage == null) {
            return null;
        }
        return endMileage - startMileage;
    }
}
