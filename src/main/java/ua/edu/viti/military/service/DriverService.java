package ua.edu.viti.military.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.viti.military.dto.request.DriverCreateDTO;
import ua.edu.viti.military.dto.request.DriverUpdateDTO;
import ua.edu.viti.military.dto.response.DriverResponseDTO;
import ua.edu.viti.military.entity.Driver;
import ua.edu.viti.military.exception.DuplicateResourceException;
import ua.edu.viti.military.exception.ResourceNotFoundException;
import ua.edu.viti.military.repository.DriverRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DriverService {

    private final DriverRepository driverRepository;

    @Transactional
    public DriverResponseDTO create(DriverCreateDTO dto) {
        log.info("Creating new driver with military ID: {}", dto.getMilitaryId());

        if (driverRepository.existsByMilitaryId(dto.getMilitaryId())) {
            throw new DuplicateResourceException(
                    "Водій з військовим ID '" + dto.getMilitaryId() + "' вже існує");
        }

        if (dto.getLicenseNumber() != null 
                && driverRepository.existsByLicenseNumber(dto.getLicenseNumber())) {
            throw new DuplicateResourceException(
                    "Водій з номером посвідчення '" + dto.getLicenseNumber() + "' вже існує");
        }

        Driver driver = new Driver();
        driver.setMilitaryId(dto.getMilitaryId());
        driver.setFirstName(dto.getFirstName());
        driver.setLastName(dto.getLastName());
        driver.setMiddleName(dto.getMiddleName());
        driver.setRank(dto.getRank());
        driver.setLicenseNumber(dto.getLicenseNumber());
        driver.setLicenseCategories(dto.getLicenseCategories());
        driver.setLicenseExpiryDate(dto.getLicenseExpiryDate());
        driver.setPhoneNumber(dto.getPhoneNumber());
        driver.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);

        Driver saved = driverRepository.save(driver);
        log.info("Driver created with ID: {}", saved.getId());

        return toResponseDTO(saved);
    }

    public DriverResponseDTO getById(Long id) {
        log.debug("Fetching driver with ID: {}", id);

        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Водія з ID " + id + " не знайдено"));

        return toResponseDTO(driver);
    }

    public List<DriverResponseDTO> getAll(Boolean isActive) {
        log.debug("Fetching all drivers with isActive: {}", isActive);

        List<Driver> drivers;

        if (isActive != null) {
            drivers = driverRepository.findByIsActive(isActive);
        } else {
            drivers = driverRepository.findAll();
        }

        return drivers.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public DriverResponseDTO update(Long id, DriverUpdateDTO dto) {
        log.info("Updating driver with ID: {}", id);

        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Водія з ID " + id + " не знайдено"));

        if (dto.getLicenseNumber() != null 
                && !dto.getLicenseNumber().equals(driver.getLicenseNumber())
                && driverRepository.existsByLicenseNumber(dto.getLicenseNumber())) {
            throw new DuplicateResourceException(
                    "Водій з номером посвідчення '" + dto.getLicenseNumber() + "' вже існує");
        }

        if (dto.getFirstName() != null) {
            driver.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            driver.setLastName(dto.getLastName());
        }
        if (dto.getMiddleName() != null) {
            driver.setMiddleName(dto.getMiddleName());
        }
        if (dto.getRank() != null) {
            driver.setRank(dto.getRank());
        }
        if (dto.getLicenseNumber() != null) {
            driver.setLicenseNumber(dto.getLicenseNumber());
        }
        if (dto.getLicenseCategories() != null) {
            driver.setLicenseCategories(dto.getLicenseCategories());
        }
        if (dto.getLicenseExpiryDate() != null) {
            driver.setLicenseExpiryDate(dto.getLicenseExpiryDate());
        }
        if (dto.getPhoneNumber() != null) {
            driver.setPhoneNumber(dto.getPhoneNumber());
        }
        if (dto.getIsActive() != null) {
            driver.setIsActive(dto.getIsActive());
        }

        Driver updated = driverRepository.save(driver);
        log.info("Driver with ID {} updated successfully", id);

        return toResponseDTO(updated);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Deleting driver with ID: {}", id);

        if (!driverRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Водія з ID " + id + " не знайдено");
        }

        driverRepository.deleteById(id);
        log.info("Driver with ID {} deleted successfully", id);
    }

    public List<DriverResponseDTO> findWithExpiredLicense() {
        log.debug("Finding drivers with expired license");

        return driverRepository.findByLicenseExpiryDateBefore(LocalDate.now())
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<DriverResponseDTO> findWithExpiringLicense(int days) {
        log.debug("Finding drivers with license expiring in {} days", days);

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(days);

        return driverRepository.findActiveDriversWithExpiringLicense(startDate, endDate)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private DriverResponseDTO toResponseDTO(Driver entity) {
        DriverResponseDTO dto = new DriverResponseDTO();
        dto.setId(entity.getId());
        dto.setMilitaryId(entity.getMilitaryId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setMiddleName(entity.getMiddleName());
        dto.setRank(entity.getRank());
        dto.setLicenseNumber(entity.getLicenseNumber());
        dto.setLicenseCategories(entity.getLicenseCategories());
        dto.setLicenseExpiryDate(entity.getLicenseExpiryDate());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setIsActive(entity.getIsActive());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public Driver getEntityById(Long id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Водія з ID " + id + " не знайдено"));
    }

    public String getFullName(Driver driver) {
        StringBuilder sb = new StringBuilder();
        sb.append(driver.getLastName()).append(" ").append(driver.getFirstName());
        if (driver.getMiddleName() != null && !driver.getMiddleName().isEmpty()) {
            sb.append(" ").append(driver.getMiddleName());
        }
        return sb.toString();
    }
}
