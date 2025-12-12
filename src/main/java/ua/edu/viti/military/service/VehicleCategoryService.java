package ua.edu.viti.military.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.viti.military.dto.request.VehicleCategoryCreateDTO;
import ua.edu.viti.military.dto.response.VehicleCategoryResponseDTO;
import ua.edu.viti.military.entity.VehicleCategory;
import ua.edu.viti.military.exception.DuplicateResourceException;
import ua.edu.viti.military.exception.ResourceNotFoundException;
import ua.edu.viti.military.repository.VehicleCategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class VehicleCategoryService {

    private final VehicleCategoryRepository categoryRepository;

    @Transactional
    public VehicleCategoryResponseDTO create(VehicleCategoryCreateDTO dto) {
        log.info("Creating new vehicle category with code: {}", dto.getCode());

        if (categoryRepository.existsByCode(dto.getCode())) {
            throw new DuplicateResourceException(
                    "Категорія з кодом '" + dto.getCode() + "' вже існує");
        }

        if (categoryRepository.existsByName(dto.getName())) {
            throw new DuplicateResourceException(
                    "Категорія з назвою '" + dto.getName() + "' вже існує");
        }

        VehicleCategory category = new VehicleCategory();
        category.setName(dto.getName());
        category.setCode(dto.getCode());
        category.setDescription(dto.getDescription());
        category.setRequiredLicense(dto.getRequiredLicense());
        category.setMaxLoadCapacity(dto.getMaxLoadCapacity());

        VehicleCategory saved = categoryRepository.save(category);
        log.info("Vehicle category created with ID: {}", saved.getId());

        return toResponseDTO(saved);
    }

    public VehicleCategoryResponseDTO getById(Long id) {
        log.debug("Fetching vehicle category with ID: {}", id);

        VehicleCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Категорію з ID " + id + " не знайдено"));

        return toResponseDTO(category);
    }

    public List<VehicleCategoryResponseDTO> getAll() {
        log.debug("Fetching all vehicle categories");

        return categoryRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public VehicleCategoryResponseDTO update(Long id, VehicleCategoryCreateDTO dto) {
        log.info("Updating vehicle category with ID: {}", id);

        VehicleCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Категорію з ID " + id + " не знайдено"));

        if (!category.getCode().equals(dto.getCode()) 
                && categoryRepository.existsByCode(dto.getCode())) {
            throw new DuplicateResourceException(
                    "Категорія з кодом '" + dto.getCode() + "' вже існує");
        }

        if (!category.getName().equals(dto.getName()) 
                && categoryRepository.existsByName(dto.getName())) {
            throw new DuplicateResourceException(
                    "Категорія з назвою '" + dto.getName() + "' вже існує");
        }

        category.setName(dto.getName());
        category.setCode(dto.getCode());
        category.setDescription(dto.getDescription());
        category.setRequiredLicense(dto.getRequiredLicense());
        category.setMaxLoadCapacity(dto.getMaxLoadCapacity());

        VehicleCategory updated = categoryRepository.save(category);
        log.info("Vehicle category with ID {} updated successfully", id);

        return toResponseDTO(updated);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Deleting vehicle category with ID: {}", id);

        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Категорію з ID " + id + " не знайдено");
        }

        categoryRepository.deleteById(id);
        log.info("Vehicle category with ID {} deleted successfully", id);
    }

    private VehicleCategoryResponseDTO toResponseDTO(VehicleCategory entity) {
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

    public VehicleCategory getEntityById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Категорію з ID " + id + " не знайдено"));
    }
}
