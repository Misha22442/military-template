package ua.edu.viti.military.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.edu.viti.military.entity.VehicleAssignment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for VehicleAssignment entity.
 */
@Repository
public interface VehicleAssignmentRepository extends JpaRepository<VehicleAssignment, Long> {

    List<VehicleAssignment> findByVehicleId(Long vehicleId);

    List<VehicleAssignment> findByDriverId(Long driverId);

    List<VehicleAssignment> findByIsActiveTrue();

    @Query("SELECT va FROM VehicleAssignment va WHERE va.vehicle.id = :vehicleId AND va.isActive = true")
    Optional<VehicleAssignment> findActiveAssignmentByVehicleId(@Param("vehicleId") Long vehicleId);

    @Query("SELECT va FROM VehicleAssignment va WHERE va.driver.id = :driverId AND va.isActive = true")
    List<VehicleAssignment> findActiveAssignmentsByDriverId(@Param("driverId") Long driverId);

    @Query("SELECT va FROM VehicleAssignment va WHERE va.assignmentDate BETWEEN :startDate AND :endDate")
    List<VehicleAssignment> findByAssignmentDateBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT va FROM VehicleAssignment va LEFT JOIN FETCH va.vehicle LEFT JOIN FETCH va.driver WHERE va.id = :id")
    Optional<VehicleAssignment> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT COUNT(va) FROM VehicleAssignment va WHERE va.driver.id = :driverId AND va.isActive = true")
    Long countActiveAssignmentsByDriverId(@Param("driverId") Long driverId);

    int countByDriverIdAndIsActiveTrue(Long driverId);

    int countByVehicleIdAndIsActiveTrue(Long vehicleId);

    @Query("SELECT va FROM VehicleAssignment va WHERE va.vehicle.id = :vehicleId ORDER BY va.assignmentDate DESC")
    List<VehicleAssignment> findByVehicleIdOrderByAssignmentDateDesc(@Param("vehicleId") Long vehicleId);

    @Query("SELECT va FROM VehicleAssignment va WHERE va.isActive = true AND va.vehicle.category.id = :categoryId")
    List<VehicleAssignment> findActiveAssignmentsByCategoryId(@Param("categoryId") Long categoryId);
}
