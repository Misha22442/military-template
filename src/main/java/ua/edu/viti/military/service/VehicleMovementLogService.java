package ua.edu.viti.military.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.viti.military.dto.request.VehicleMovementRequestDto;
import ua.edu.viti.military.dto.response.VehicleMovementResponseDto;
import ua.edu.viti.military.entity.*;
import ua.edu.viti.military.event.VehicleAssignedEvent;
import ua.edu.viti.military.event.VehicleMaintenanceEvent;
import ua.edu.viti.military.event.VehicleStatusChangedEvent;
import ua.edu.viti.military.exception.BusinessLogicException;
import ua.edu.viti.military.exception.ResourceNotFoundException;
import ua.edu.viti.military.repository.DriverRepository;
import ua.edu.viti.military.repository.VehicleMovementLogRepository;
import ua.edu.viti.military.repository.VehicleRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Service for vehicle movement log operations with transactional management.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class VehicleMovementLogService {
    
    private final VehicleMovementLogRepository movementLogRepository;
    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;
    private final ApplicationEventPublisher eventPublisher;
    
    /**
     * Assign driver to vehicle.
     * Transaction with REPEATABLE_READ isolation to prevent phantom reads.
     */
    @Transactional(
        isolation = Isolation.REPEATABLE_READ,
        rollbackFor = Exception.class
    )
    public VehicleMovementResponseDto assignDriver(VehicleMovementRequestDto dto) {
        log.info("Assigning driver {} to vehicle {}", dto.getDriverId(), dto.getVehicleId());
        Objects.requireNonNull(dto.getVehicleId(), "vehicleId must not be null");

        // 1. Find vehicle
        Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId())
            .orElseThrow(() -> new ResourceNotFoundException("Транспорт не знайдено"));

        // 2. Find driver
        Objects.requireNonNull(dto.getDriverId(), "driverId must not be null");
        Driver driver = driverRepository.findById(dto.getDriverId())
            .orElseThrow(() -> new ResourceNotFoundException("Водія не знайдено"));

        // 3. Validate driver status
        if (!driver.getIsActive()) {
            throw new BusinessLogicException("Водій не активний");
        }
        
        // 4. Check if vehicle already has a driver
        if (vehicle.getDriver() != null) {
            throw new BusinessLogicException(
                "Транспорт вже призначено водію: " + vehicle.getDriver().getFirstName() + " " + vehicle.getDriver().getLastName()
            );
        }
        
        // 5. Assign driver to vehicle
        String oldStatus = vehicle.getStatus().name();
        vehicle.setDriver(driver);
        vehicle.setStatus(VehicleStatus.OPERATIONAL);
        vehicleRepository.save(vehicle);
        
        // 6. Create movement log
        VehicleMovementLog movementLog = createMovementLog(
            vehicle, driver, MovementType.ASSIGN_DRIVER, dto
        );
        movementLog.setDescription("Призначення водія: " + driver.getFirstName() + " " + driver.getLastName());
        VehicleMovementLog saved = Objects.requireNonNull(movementLogRepository.save(movementLog));
        
        // 7. Publish event
        eventPublisher.publishEvent(new VehicleAssignedEvent(this, vehicle, driver));
        eventPublisher.publishEvent(new VehicleStatusChangedEvent(this, vehicle, oldStatus, vehicle.getStatus().name()));
        
        log.info("Driver assigned successfully. Movement log ID: {}", saved.getId());
        
        return toResponseDto(saved);
    }
    
    /**
     * Unassign driver from vehicle.
     */
    @Transactional(
        isolation = Isolation.REPEATABLE_READ,
        rollbackFor = Exception.class
    )
    public VehicleMovementResponseDto unassignDriver(Long vehicleId, VehicleMovementRequestDto dto) {
        log.info("Unassigning driver from vehicle {}", vehicleId);
        Objects.requireNonNull(vehicleId, "vehicleId must not be null");

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
            .orElseThrow(() -> new ResourceNotFoundException("Транспорт не знайдено"));
        
        if (vehicle.getDriver() == null) {
            throw new BusinessLogicException("Транспорт не має призначеного водія");
        }
        
        Driver previousDriver = vehicle.getDriver();
        String oldStatus = vehicle.getStatus().name();
        
        vehicle.setDriver(null);
        vehicle.setStatus(VehicleStatus.OPERATIONAL);
        vehicleRepository.save(vehicle);
        
        VehicleMovementLog movementLog = createMovementLog(
            vehicle, previousDriver, MovementType.UNASSIGN_DRIVER, dto
        );
        movementLog.setDescription("Відкликання водія: " + previousDriver.getFirstName() + " " + previousDriver.getLastName());
        VehicleMovementLog saved = Objects.requireNonNull(movementLogRepository.save(movementLog));
        
        eventPublisher.publishEvent(new VehicleStatusChangedEvent(this, vehicle, oldStatus, vehicle.getStatus().name()));
        
        log.info("Driver unassigned successfully. Movement log ID: {}", saved.getId());
        
        return toResponseDto(saved);
    }
    
    /**
     * Send vehicle to maintenance.
     */
    @Transactional(
        isolation = Isolation.REPEATABLE_READ,
        rollbackFor = Exception.class
    )
    public VehicleMovementResponseDto sendToMaintenance(VehicleMovementRequestDto dto) {
        log.info("Sending vehicle {} to maintenance", dto.getVehicleId());
        Objects.requireNonNull(dto.getVehicleId(), "vehicleId must not be null");

        Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId())
            .orElseThrow(() -> new ResourceNotFoundException("Транспорт не знайдено"));
        
        if (vehicle.getStatus() == VehicleStatus.IN_MAINTENANCE) {
            throw new BusinessLogicException("Транспорт вже на технічному обслуговуванні");
        }
        
        String oldStatus = vehicle.getStatus().name();
        vehicle.setStatus(VehicleStatus.IN_MAINTENANCE);
        vehicleRepository.save(vehicle);
        
        VehicleMovementLog movementLog = createMovementLog(
            vehicle, vehicle.getDriver(), MovementType.SEND_TO_MAINTENANCE, dto
        );
        movementLog.setMaintenanceType(dto.getMaintenanceType() != null ? dto.getMaintenanceType() : "Планове ТО");
        movementLog.setDescription("Відправлення в ТО: " + movementLog.getMaintenanceType());
        VehicleMovementLog saved = Objects.requireNonNull(movementLogRepository.save(movementLog));
        
        eventPublisher.publishEvent(new VehicleMaintenanceEvent(this, vehicle, movementLog.getMaintenanceType()));
        eventPublisher.publishEvent(new VehicleStatusChangedEvent(this, vehicle, oldStatus, vehicle.getStatus().name()));
        
        log.info("Vehicle sent to maintenance. Movement log ID: {}", saved.getId());
        
        return toResponseDto(saved);
    }
    
    /**
     * Return vehicle from maintenance.
     */
    @Transactional(
        isolation = Isolation.REPEATABLE_READ,
        rollbackFor = Exception.class
    )
    public VehicleMovementResponseDto returnFromMaintenance(Long vehicleId, VehicleMovementRequestDto dto) {
        log.info("Returning vehicle {} from maintenance", vehicleId);
        Objects.requireNonNull(vehicleId, "vehicleId must not be null");

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
            .orElseThrow(() -> new ResourceNotFoundException("Транспорт не знайдено"));
        
        if (vehicle.getStatus() != VehicleStatus.IN_MAINTENANCE) {
            throw new BusinessLogicException("Транспорт не на технічному обслуговуванні");
        }
        
        String oldStatus = vehicle.getStatus().name();
        vehicle.setStatus(VehicleStatus.OPERATIONAL);
        vehicleRepository.save(vehicle);
        
        VehicleMovementLog movementLog = createMovementLog(
            vehicle, vehicle.getDriver(), MovementType.RETURN_FROM_MAINTENANCE, dto
        );
        movementLog.setDescription("Повернення з ТО");
        VehicleMovementLog saved = Objects.requireNonNull(movementLogRepository.save(movementLog));
        
        eventPublisher.publishEvent(new VehicleStatusChangedEvent(this, vehicle, oldStatus, vehicle.getStatus().name()));
        
        log.info("Vehicle returned from maintenance. Movement log ID: {}", saved.getId());
        
        return toResponseDto(saved);
    }
    
    /**
     * Get movement history for a vehicle.
     */
    public List<VehicleMovementResponseDto> getVehicleHistory(Long vehicleId) {
        Objects.requireNonNull(vehicleId, "vehicleId must not be null");
        return movementLogRepository.findByVehicleIdOrderByPerformedAtDesc(vehicleId)
            .stream()
            .map(this::toResponseDto)
            .collect(Collectors.toList());
    }
    
    /**
     * Get movement history for a driver.
     */
    public List<VehicleMovementResponseDto> getDriverHistory(Long driverId) {
        Objects.requireNonNull(driverId, "driverId must not be null");
        return movementLogRepository.findByDriverIdOrderByPerformedAtDesc(driverId)
            .stream()
            .map(this::toResponseDto)
            .collect(Collectors.toList());
    }
    
    /**
     * Get recent activity.
     */
    public List<VehicleMovementResponseDto> getRecentActivity(int limit) {
        return movementLogRepository.findRecentActivity(
            org.springframework.data.domain.PageRequest.of(0, limit)
        ).stream()
            .map(this::toResponseDto)
            .collect(Collectors.toList());
    }
    
    // Helper methods
    
    private VehicleMovementLog createMovementLog(
            Vehicle vehicle, 
            Driver driver, 
            MovementType type, 
            VehicleMovementRequestDto dto) {
        
        VehicleMovementLog log = new VehicleMovementLog();
        log.setVehicle(vehicle);
        log.setDriver(driver);
        log.setType(type);
        log.setDescription(dto.getDescription());
        log.setDestination(dto.getDestination());
        log.setMileageAtOperation(dto.getMileageAtOperation() != null ? dto.getMileageAtOperation() : vehicle.getMileage());
        log.setFuelLevel(dto.getFuelLevel());
        log.setMaintenanceType(dto.getMaintenanceType());
        log.setNotes(dto.getNotes());
        log.setPerformedBy(getCurrentUser());
        
        return log;
    }
    
    private String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        
        return "system";
    }
    
    private VehicleMovementResponseDto toResponseDto(VehicleMovementLog log) {
        return VehicleMovementResponseDto.builder()
            .id(log.getId())
            .vehicleId(log.getVehicle().getId())
            .vehicleRegistrationNumber(log.getVehicle().getRegistrationNumber())
            .driverId(log.getDriver() != null ? log.getDriver().getId() : null)
            .driverName(log.getDriver() != null ? 
                log.getDriver().getFirstName() + " " + log.getDriver().getLastName() : null)
            .type(log.getType())
            .description(log.getDescription())
            .destination(log.getDestination())
            .mileageAtOperation(log.getMileageAtOperation())
            .fuelLevel(log.getFuelLevel())
            .maintenanceType(log.getMaintenanceType())
            .notes(log.getNotes())
            .performedBy(log.getPerformedBy())
            .performedAt(log.getPerformedAt())
            .build();
    }
}
