package ua.edu.viti.military.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.edu.viti.military.entity.FuelType;
import ua.edu.viti.military.entity.Vehicle;
import ua.edu.viti.military.entity.VehicleStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByRegistrationNumber(String registrationNumber);

    boolean existsByRegistrationNumber(String registrationNumber);

    Optional<Vehicle> findByEngineNumber(String engineNumber);

    boolean existsByEngineNumber(String engineNumber);

    Optional<Vehicle> findByChassisNumber(String chassisNumber);

    boolean existsByChassisNumber(String chassisNumber);

    List<Vehicle> findByCategoryId(Long categoryId);

    List<Vehicle> findByStatus(VehicleStatus status);

    long countByStatus(VehicleStatus status);

    List<Vehicle> findByDriverId(Long driverId);

    List<Vehicle> findByFuelType(FuelType fuelType);

    List<Vehicle> findByMileageBetween(Integer minMileage, Integer maxMileage);

    @Query("SELECT v FROM Vehicle v WHERE " +
           "(v.mileage - v.lastMaintenanceMileage) >= v.maintenanceIntervalKm " +
           "AND v.status = 'OPERATIONAL'")
    List<Vehicle> findVehiclesRequiringMaintenance();

    @Query("SELECT v FROM Vehicle v JOIN FETCH v.category WHERE v.status = :status")
    List<Vehicle> findByStatusWithCategory(@Param("status") VehicleStatus status);

    @Query("SELECT v FROM Vehicle v " +
           "JOIN FETCH v.category " +
           "LEFT JOIN FETCH v.driver")
    List<Vehicle> findAllWithCategoryAndDriver();

    List<Vehicle> findByStatusAndCategoryId(VehicleStatus status, Long categoryId);
}
