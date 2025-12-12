package ua.edu.viti.military.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.edu.viti.military.dto.request.VehicleCategoryCreateDTO;
import ua.edu.viti.military.dto.response.VehicleCategoryResponseDTO;
import ua.edu.viti.military.exception.ErrorResponse;
import ua.edu.viti.military.service.VehicleCategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/vehicle-categories")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Vehicle Categories", description = "API для управління категоріями транспорту")
public class VehicleCategoryController {

    private final VehicleCategoryService categoryService;

    @Operation(
            summary = "Створити нову категорію",
            description = "Створює нову категорію транспортного засобу з унікальним кодом та назвою"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Категорію успішно створено",
                    content = @Content(schema = @Schema(implementation = VehicleCategoryResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Невалідні дані",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Категорія з таким кодом або назвою вже існує",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<VehicleCategoryResponseDTO> create(
            @Valid @RequestBody VehicleCategoryCreateDTO dto) {
        log.info("POST /api/vehicle-categories - Creating new category");
        VehicleCategoryResponseDTO created = categoryService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(
            summary = "Отримати категорію за ID",
            description = "Повертає детальну інформацію про категорію за її унікальним ідентифікатором"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Категорію знайдено",
                    content = @Content(schema = @Schema(implementation = VehicleCategoryResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Категорію не знайдено",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<VehicleCategoryResponseDTO> getById(
            @Parameter(description = "ID категорії") @PathVariable Long id) {
        log.info("GET /api/vehicle-categories/{} - Fetching category", id);
        return ResponseEntity.ok(categoryService.getById(id));
    }

    @Operation(
            summary = "Отримати всі категорії",
            description = "Повертає список всіх категорій транспортних засобів"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список категорій",
                    content = @Content(schema = @Schema(implementation = VehicleCategoryResponseDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<VehicleCategoryResponseDTO>> getAll() {
        log.info("GET /api/vehicle-categories - Fetching all categories");
        return ResponseEntity.ok(categoryService.getAll());
    }

    @Operation(
            summary = "Оновити категорію",
            description = "Оновлює інформацію про категорію за її ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Категорію успішно оновлено",
                    content = @Content(schema = @Schema(implementation = VehicleCategoryResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Невалідні дані",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Категорію не знайдено",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Категорія з таким кодом або назвою вже існує",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<VehicleCategoryResponseDTO> update(
            @Parameter(description = "ID категорії") @PathVariable Long id,
            @Valid @RequestBody VehicleCategoryCreateDTO dto) {
        log.info("PUT /api/vehicle-categories/{} - Updating category", id);
        return ResponseEntity.ok(categoryService.update(id, dto));
    }

    @Operation(
            summary = "Видалити категорію",
            description = "Видаляє категорію за її ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Категорію успішно видалено"),
            @ApiResponse(responseCode = "404", description = "Категорію не знайдено",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID категорії") @PathVariable Long id) {
        log.info("DELETE /api/vehicle-categories/{} - Deleting category", id);
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
