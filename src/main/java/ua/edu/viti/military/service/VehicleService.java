package ua.edu.viti.military.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.viti.military.dto.request.VehicleCreateDTO;
import ua.edu.viti.military.dto.request.VehicleUpdateDTO;
import ua.edu.viti.military.dto.response.VehicleCategoryResponseDTO;
import ua.edu.viti.military.dto.response.VehicleResponseDTO;
import ua.edu.viti.military.entity.Driver;
import ua.edu.viti.military.entity.Vehicle;
import ua.edu.viti.military.entity.VehicleCategory;
import ua.edu.viti.military.entity.VehicleStatus;
import ua.edu.viti.military.exception.BusinessLogicException;
import ua.edu.viti.military.exception.DuplicateResourceException;
import ua.edu.viti.military.exception.ResourceNotFoundException;
import ua.edu.viti.military.repository.VehicleRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleCategoryService categoryService;
    private final DriverService driverService;

    @Transactional
    public VehicleResponseDTO create(VehicleCreateDTO dto) {
        log.info("Creating new vehicle with registration number: {}", dto.getRegistrationNumber());

        if (vehicleRepository.existsByRegistrationNumber(dto.getRegistrationNumber())) {
            throw new DuplicateResourceException(
                    "Транспорт з реєстраційним номером '" + dto.getRegistrationNumber() + "' вже існує");
        }

        if (dto.getEngineNumber() != null 
                && vehicleRepository.existsByEngineNumber(dto.getEngineNumber())) {
            throw new DuplicateResourceException(
                    "Транспорт з номером двигуна '" + dto.getEngineNumber() + "' вже існує");
        }

        if (dto.getChassisNumber() != null 
                && vehicleRepository.existsByChassisNumber(dto.getChassisNumber())) {
            throw new DuplicateResourceException(
                    "Транспорт з номером шасі '" + dto.getChassisNumber() + "' вже існує");
        }

        VehicleCategory category = categoryService.getEntityById(dto.getCategoryId());

        Driver driver = null;
        if (dto.getDriverId() != null) {
            driver = driverService.getEntityById(dto.getDriverId());
            validateDriverAssignment(driver, category);
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setModel(dto.getModel());
        vehicle.setRegistrationNumber(dto.getRegistrationNumber());
        vehicle.setCategory(category);
        vehicle.setEngineNumber(dto.getEngineNumber());
        vehicle.setChassisNumber(dto.getChassisNumber());
        vehicle.setManufactureYear(dto.getManufactureYear());
        vehicle.setMileage(dto.getMileage() != null ? dto.getMileage() : 0);
        vehicle.setFuelType(dto.getFuelType());
        vehicle.setFuelConsumption(dto.getFuelConsumption());
        vehicle.setMaintenanceIntervalKm(dto.getMaintenanceIntervalKm() != null 
                ? dto.getMaintenanceIntervalKm() : 10000);
        vehicle.setLastMaintenanceDate(dto.getLastMaintenanceDate());
        vehicle.setLastMaintenanceMileage(dto.getLastMaintenanceMileage() != null 
                ? dto.getLastMaintenanceMileage() : 0);
        vehicle.setDriver(driver);
        vehicle.setStatus(VehicleStatus.OPERATIONAL);

        Vehicle saved = vehicleRepository.save(vehicle);
        log.info("Vehicle created with ID: {}", saved.getId());

        return toResponseDTO(saved);
    }

    public VehicleResponseDTO getById(Long id) {
        log.debug("Fetching vehicle with ID: {}", id);

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Транспорт з ID " + id + " не знайдено"));

        return toResponseDTO(vehicle);
    }

    public List<VehicleResponseDTO> getAll(VehicleStatus status, Long categoryId) {
        log.debug("Fetching all vehicles with status: {}, categoryId: {}", status, categoryId);

        List<Vehicle> vehicles;

        if (status != null && categoryId != null) {
            vehicles = vehicleRepository.findByStatusAndCategoryId(status, categoryId);
        } else if (status != null) {
            vehicles = vehicleRepository.findByStatus(status);
        } else if (categoryId != null) {
            vehicles = vehicleRepository.findByCategoryId(categoryId);
        } else {
            vehicles = vehicleRepository.findAllWithCategoryAndDriver();
        }

        return vehicles.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public VehicleResponseDTO update(Long id, VehicleUpdateDTO dto) {
        log.info("Updating vehicle with ID: {}", id);

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Транспорт з ID " + id + " не знайдено"));

        if (dto.getEngineNumber() != null 
                && !dto.getEngineNumber().equals(vehicle.getEngineNumber())
                && vehicleRepository.existsByEngineNumber(dto.getEngineNumber())) {
            throw new DuplicateResourceException(
                    "Транспорт з номером двигуна '" + dto.getEngineNumber() + "' вже існує");
        }

        if (dto.getChassisNumber() != null 
                && !dto.getChassisNumber().equals(vehicle.getChassisNumber())
                && vehicleRepository.existsByChassisNumber(dto.getChassisNumber())) {
            throw new DuplicateResourceException(
                    "Транспорт з номером шасі '" + dto.getChassisNumber() + "' вже існує");
        }

        if (dto.getModel() != null) {
            vehicle.setModel(dto.getModel());
        }
        if (dto.getCategoryId() != null) {
            VehicleCategory category = categoryService.getEntityById(dto.getCategoryId());
            vehicle.setCategory(category);
        }
        if (dto.getEngineNumber() != null) {
            vehicle.setEngineNumber(dto.getEngineNumber());
        }
        if (dto.getChassisNumber() != null) {
            vehicle.setChassisNumber(dto.getChassisNumber());
        }
        if (dto.getMileage() != null) {
            vehicle.setMileage(dto.getMileage());
        }
        if (dto.getFuelType() != null) {
            vehicle.setFuelType(dto.getFuelType());
        }
        if (dto.getFuelConsumption() != null) {
            vehicle.setFuelConsumption(dto.getFuelConsumption());
        }
        if (dto.getMaintenanceIntervalKm() != null) {
            vehicle.setMaintenanceIntervalKm(dto.getMaintenanceIntervalKm());
        }
        if (dto.getLastMaintenanceDate() != null) {
            vehicle.setLastMaintenanceDate(dto.getLastMaintenanceDate());
        }
        if (dto.getLastMaintenanceMileage() != null) {
            vehicle.setLastMaintenanceMileage(dto.getLastMaintenanceMileage());
        }
        if (dto.getDriverId() != null) {
            Driver driver = driverService.getEntityById(dto.getDriverId());
            validateDriverAssignment(driver, vehicle.getCategory());
            vehicle.setDriver(driver);
        }
        if (dto.getStatus() != null) {
            vehicle.setStatus(dto.getStatus());
        }

        Vehicle updated = vehicleRepository.save(vehicle);
        log.info("Vehicle with ID {} updated successfully", id);

        return toResponseDTO(updated);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Deleting vehicle with ID: {}", id);

        if (!vehicleRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Транспорт з ID " + id + " не знайдено");
        }

        vehicleRepository.deleteById(id);
        log.info("Vehicle with ID {} deleted successfully", id);
    }

    public Vehicle getEntityById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Транспорт з ID " + id + " не знайдено"));
    }

    public void validateVehicleForAssignment(Vehicle vehicle) {
        if (vehicle.getStatus() != VehicleStatus.OPERATIONAL) {
            throw new BusinessLogicException(
                    "Транспорт не є операційним та не може бути призначений");
        }
    }

    public List<VehicleResponseDTO> findRequiringMaintenance() {
        log.debug("Finding vehicles requiring maintenance");

        return vehicleRepository.findVehiclesRequiringMaintenance()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<VehicleResponseDTO> findByDriverId(Long driverId) {
        log.debug("Finding vehicles by driver ID: {}", driverId);

        return vehicleRepository.findByDriverId(driverId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private void validateDriverAssignment(Driver driver, VehicleCategory category) {
        if (!driver.getIsActive()) {
            throw new BusinessLogicException(
                    "Не можна призначити неактивного водія на транспорт");
        }

        if (category.getRequiredLicense() != null && driver.getLicenseCategories() != null) {
            String requiredLicense = category.getRequiredLicense();
            String driverCategories = driver.getLicenseCategories();
            
            if (!driverCategories.contains(requiredLicense)) {
                throw new BusinessLogicException(
                        String.format("Водій не має необхідної категорії прав '%s'. " +
                                "Наявні категорії: '%s'", requiredLicense, driverCategories));
            }
        }
    }

    public void validateMaintenanceStatus(Vehicle vehicle) {
        if (vehicle.getLastMaintenanceMileage() == null || vehicle.getMaintenanceIntervalKm() == null) {
            return;
        }
        
        int kmSinceLastMaintenance = vehicle.getMileage() - vehicle.getLastMaintenanceMileage();

        if (kmSinceLastMaintenance >= vehicle.getMaintenanceIntervalKm()) {
            throw new BusinessLogicException(
                    String.format("Транспорт '%s' потребує ТО! Пробіг після останнього ТО: %d км. " +
                            "Інтервал ТО: %d км",
                            vehicle.getRegistrationNumber(),
                            kmSinceLastMaintenance,
                            vehicle.getMaintenanceIntervalKm()));
        }
    }

    private VehicleResponseDTO toResponseDTO(Vehicle entity) {
        VehicleResponseDTO dto = new VehicleResponseDTO();
        dto.setId(entity.getId());
        dto.setModel(entity.getModel());
        dto.setRegistrationNumber(entity.getRegistrationNumber());

        dto.setCategory(toCategoryDTO(entity.getCategory()));

        dto.setEngineNumber(entity.getEngineNumber());
        dto.setChassisNumber(entity.getChassisNumber());
        dto.setManufactureYear(entity.getManufactureYear());
        dto.setMileage(entity.getMileage());
        dto.setFuelType(entity.getFuelType());
        dto.setFuelConsumption(entity.getFuelConsumption());
        dto.setMaintenanceIntervalKm(entity.getMaintenanceIntervalKm());
        dto.setLastMaintenanceDate(entity.getLastMaintenanceDate());
        dto.setLastMaintenanceMileage(entity.getLastMaintenanceMileage());

        if (entity.getDriver() != null) {
            dto.setDriverId(entity.getDriver().getId());
            dto.setDriverFullName(driverService.getFullName(entity.getDriver()));
        }

        dto.setStatus(entity.getStatus());

        if (entity.getMileage() != null && entity.getLastMaintenanceMileage() != null 
                && entity.getMaintenanceIntervalKm() != null) {
            int kmSinceLastMaintenance = entity.getMileage() - entity.getLastMaintenanceMileage();
            dto.setKmUntilMaintenance(entity.getMaintenanceIntervalKm() - kmSinceLastMaintenance);
        }

        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }

    private VehicleCategoryResponseDTO toCategoryDTO(VehicleCategory entity) {
        VehicleCategoryResponseDTO dto = new VehicleCategoryResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCode(entity.getCode());
        dto.setDescription(entity.getDescription());
        dto.setRequiredLicense(entity.getRequiredLicense());
        dto.setMaxLoadCapacity(entity.getMaxLoadCapacity());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
