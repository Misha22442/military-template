package ua.edu.viti.military.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.viti.military.dto.VehicleAssignmentCreateDto;
import ua.edu.viti.military.dto.VehicleAssignmentEndDto;
import ua.edu.viti.military.dto.VehicleAssignmentResponseDto;
import ua.edu.viti.military.entity.Driver;
import ua.edu.viti.military.entity.Vehicle;
import ua.edu.viti.military.entity.VehicleAssignment;
import ua.edu.viti.military.entity.VehicleCategory;
import ua.edu.viti.military.exception.BusinessRuleException;
import ua.edu.viti.military.exception.DriverCompatibilityException;
import ua.edu.viti.military.exception.ResourceNotFoundException;
import ua.edu.viti.military.mapper.VehicleAssignmentMapper;
import ua.edu.viti.military.repository.VehicleAssignmentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Service for managing vehicle assignments.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class VehicleAssignmentService {

    private final VehicleAssignmentRepository assignmentRepository;
    private final VehicleService vehicleService;
    private final DriverService driverService;
    private final VehicleAssignmentMapper assignmentMapper;

    /**
     * Create a new vehicle assignment.
     */
    @Transactional
    public VehicleAssignmentResponseDto create(VehicleAssignmentCreateDto dto) {
        log.info("Creating new assignment for vehicle ID: {} and driver ID: {}", dto.getVehicleId(), dto.getDriverId());

        Vehicle vehicle = vehicleService.getEntityById(dto.getVehicleId());
        Driver driver = driverService.getEntityById(dto.getDriverId());

        // Validate vehicle is available for assignment
        vehicleService.validateVehicleForAssignment(vehicle);

        // Check if vehicle already has an active assignment
        if (assignmentRepository.findActiveAssignmentByVehicleId(vehicle.getId()).isPresent()) {
            throw new BusinessRuleException("Транспорт вже має активне призначення");
        }

        // Validate driver is active
        if (!driver.getIsActive()) {
            throw new BusinessRuleException("Водій не є активним та не може бути призначений");
        }

        // Validate driver's license is valid
        if (!driver.isLicenseValid()) {
            throw new BusinessRuleException("Ліцензія водія прострочена");
        }

        // Validate driver has required license category
        validateDriverCompatibility(driver, vehicle.getCategory());

        VehicleAssignment assignment = assignmentMapper.toEntity(dto, vehicle, driver);
        assignment.setStartMileage(vehicle.getMileage());

        VehicleAssignment saved = Objects.requireNonNull(assignmentRepository.save(assignment));

        log.info("Assignment created with ID: {}", saved.getId());
        return assignmentMapper.toResponseDto(saved);
    }

    /**
     * Get assignment by ID.
     */
    public VehicleAssignmentResponseDto getById(Long id) {
        log.debug("Fetching assignment with ID: {}", id);

        Objects.requireNonNull(id, "id must not be null");
        VehicleAssignment assignment = Objects.requireNonNull(assignmentRepository.findByIdWithDetails(id)
            .orElseThrow(() -> new ResourceNotFoundException("Призначення", id)));

        return assignmentMapper.toResponseDto(assignment);
    }

    /**
     * Get all assignments.
     */
    public List<VehicleAssignmentResponseDto> getAll() {
        log.debug("Fetching all assignments");

        return assignmentRepository.findAll().stream()
                .map(assignmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Get active assignments.
     */
    public List<VehicleAssignmentResponseDto> getActiveAssignments() {
        log.debug("Fetching active assignments");

        return assignmentRepository.findByIsActiveTrue().stream()
                .map(assignmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Get assignments by vehicle.
     */
    public List<VehicleAssignmentResponseDto> getByVehicle(Long vehicleId) {
        log.debug("Fetching assignments for vehicle ID: {}", vehicleId);

        return assignmentRepository.findByVehicleIdOrderByAssignmentDateDesc(vehicleId).stream()
                .map(assignmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Get assignments by driver.
     */
    public List<VehicleAssignmentResponseDto> getByDriver(Long driverId) {
        log.debug("Fetching assignments for driver ID: {}", driverId);

        return assignmentRepository.findByDriverId(driverId).stream()
                .map(assignmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Get active assignment by vehicle.
     */
    public VehicleAssignmentResponseDto getActiveByVehicle(Long vehicleId) {
        log.debug("Fetching active assignment for vehicle ID: {}", vehicleId);

        Objects.requireNonNull(vehicleId, "vehicleId must not be null");
        VehicleAssignment assignment = Objects.requireNonNull(assignmentRepository.findActiveAssignmentByVehicleId(vehicleId)
            .orElseThrow(() -> new ResourceNotFoundException("Активне призначення для транспорту", vehicleId)));

        return assignmentMapper.toResponseDto(assignment);
    }

    /**
     * Get active assignments by driver.
     */
    public List<VehicleAssignmentResponseDto> getActiveByDriver(Long driverId) {
        log.debug("Fetching active assignments for driver ID: {}", driverId);

        return assignmentRepository.findActiveAssignmentsByDriverId(driverId).stream()
                .map(assignmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Get assignments by category.
     */
    public List<VehicleAssignmentResponseDto> getActiveByCategory(Long categoryId) {
        log.debug("Fetching active assignments for category ID: {}", categoryId);

        return assignmentRepository.findActiveAssignmentsByCategoryId(categoryId).stream()
                .map(assignmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Get assignments within date range.
     */
    public List<VehicleAssignmentResponseDto> getByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Fetching assignments between {} and {}", startDate, endDate);

        return assignmentRepository.findByAssignmentDateBetween(startDate, endDate).stream()
                .map(assignmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * End an assignment.
     */
    @Transactional
    public VehicleAssignmentResponseDto endAssignment(Long id, VehicleAssignmentEndDto dto) {
        log.info("Ending assignment with ID: {}", id);

        Objects.requireNonNull(id, "id must not be null");
        VehicleAssignment assignment = Objects.requireNonNull(assignmentRepository.findByIdWithDetails(id)
            .orElseThrow(() -> new ResourceNotFoundException("Призначення", id)));

        if (!assignment.getIsActive()) {
            throw new BusinessRuleException("Призначення вже завершено");
        }

        // Validate end mileage
        if (dto.getEndMileage() < assignment.getStartMileage()) {
            throw new BusinessRuleException("Кінцевий пробіг не може бути меншим за початковий");
        }

        assignment.endAssignment(dto.getEndMileage());

        // Update vehicle mileage
        Vehicle vehicle = assignment.getVehicle();
        vehicle.setMileage(dto.getEndMileage());

        VehicleAssignment updated = Objects.requireNonNull(assignmentRepository.save(assignment));

        log.info("Assignment with ID {} ended successfully", id);
        return assignmentMapper.toResponseDto(updated);
    }

    /**
     * Delete an assignment.
     */
    @Transactional
    public void delete(Long id) {
        log.info("Deleting assignment with ID: {}", id);
        Objects.requireNonNull(id, "id must not be null");
        if (!assignmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Призначення", id);
        }

        assignmentRepository.deleteById(Objects.requireNonNull(id));
        log.info("Assignment with ID {} deleted successfully", id);
    }

    /**
     * Validate driver compatibility with vehicle category.
     */
    private void validateDriverCompatibility(Driver driver, VehicleCategory category) {
        String requiredLicense = category.getRequiredLicense();

        if (requiredLicense != null && !requiredLicense.isEmpty()) {
            if (!driver.hasLicenseCategory(requiredLicense)) {
                throw new DriverCompatibilityException(
                        String.format("Водій не має необхідної категорії ліцензії '%s' для категорії транспорту '%s'",
                                requiredLicense, category.getName())
                );
            }
        }
    }
}
