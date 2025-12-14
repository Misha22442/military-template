package ua.edu.viti.military.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.edu.viti.military.entity.MovementType;
import ua.edu.viti.military.entity.VehicleMovementLog;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for VehicleMovementLog entity operations.
 */
@Repository
public interface VehicleMovementLogRepository extends JpaRepository<VehicleMovementLog, Long> {
    
    /**
     * Find all movement logs for a specific vehicle, ordered by most recent first.
     */
    List<VehicleMovementLog> findByVehicleIdOrderByPerformedAtDesc(Long vehicleId);
    
    /**
     * Find movement logs by type.
     */
    List<VehicleMovementLog> findByType(MovementType type);
    
    /**
     * Find movement logs within a time period.
     */
    List<VehicleMovementLog> findByPerformedAtBetween(
        LocalDateTime start, 
        LocalDateTime end
    );
    
    /**
     * Find movement logs for a specific driver.
     */
    List<VehicleMovementLog> findByDriverIdOrderByPerformedAtDesc(Long driverId);
    
    /**
     * Count operations by type within a time period.
     */
    @Query("SELECT COUNT(m) FROM VehicleMovementLog m " +
           "WHERE m.type = :type AND m.performedAt BETWEEN :start AND :end")
    Long countByTypeAndPeriod(
        @Param("type") MovementType type,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );
    
    /**
     * Get recent activity for all vehicles.
     */
    @Query("SELECT m FROM VehicleMovementLog m " +
           "ORDER BY m.performedAt DESC")
    List<VehicleMovementLog> findRecentActivity(org.springframework.data.domain.Pageable pageable);
}
