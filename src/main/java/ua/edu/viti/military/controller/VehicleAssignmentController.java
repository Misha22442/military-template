package ua.edu.viti.military.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.edu.viti.military.dto.VehicleAssignmentCreateDto;
import ua.edu.viti.military.dto.VehicleAssignmentEndDto;
import ua.edu.viti.military.dto.VehicleAssignmentResponseDto;
import ua.edu.viti.military.service.VehicleAssignmentService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST Controller for managing vehicle assignments.
 */
@RestController
@RequestMapping("/api/vehicle-assignments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Vehicle Assignments", description = "API для управління призначеннями транспорту водіям")
public class VehicleAssignmentController {

    private final VehicleAssignmentService assignmentService;

    @PostMapping
    @Operation(
            summary = "Створити призначення",
            description = """
                    Призначає транспорт водію.
                    
                    **Бізнес-правила:**
                    - Транспорт має бути справним
                    - Транспорт не повинен мати прострочене ТО
                    - Транспорт не повинен мати активного призначення
                    - Водій має бути активним
                    - Ліцензія водія має бути дійсною
                    - Водій має мати необхідну категорію ліцензії для цього транспорту
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Призначення успішно створено"),
            @ApiResponse(responseCode = "400", description = "Порушення бізнес-правил або помилка валідації"),
            @ApiResponse(responseCode = "404", description = "Транспорт або водія не знайдено")
    })
    public ResponseEntity<VehicleAssignmentResponseDto> create(
            @Valid @RequestBody VehicleAssignmentCreateDto dto) {
        log.info("REST request to create assignment - vehicle: {}, driver: {}", 
                dto.getVehicleId(), dto.getDriverId());
        VehicleAssignmentResponseDto created = assignmentService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Отримати призначення за ID",
            description = "Повертає детальну інформацію про призначення"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Призначення знайдено"),
            @ApiResponse(responseCode = "404", description = "Призначення не знайдено")
    })
    public ResponseEntity<VehicleAssignmentResponseDto> getById(
            @Parameter(description = "ID призначення", required = true)
            @PathVariable Long id) {
        log.debug("REST request to get assignment by ID: {}", id);
        return ResponseEntity.ok(assignmentService.getById(id));
    }

    @GetMapping
    @Operation(
            summary = "Отримати список призначень",
            description = "Повертає список всіх призначень"
    )
    @ApiResponse(responseCode = "200", description = "Список призначень успішно отримано")
    public ResponseEntity<List<VehicleAssignmentResponseDto>> getAll() {
        log.debug("REST request to get all assignments");
        return ResponseEntity.ok(assignmentService.getAll());
    }

    @GetMapping("/active")
    @Operation(
            summary = "Отримати активні призначення",
            description = "Повертає список активних (незавершених) призначень"
    )
    @ApiResponse(responseCode = "200", description = "Список призначень отримано")
    public ResponseEntity<List<VehicleAssignmentResponseDto>> getActiveAssignments() {
        log.debug("REST request to get active assignments");
        return ResponseEntity.ok(assignmentService.getActiveAssignments());
    }

    @GetMapping("/by-vehicle/{vehicleId}")
    @Operation(
            summary = "Отримати призначення для транспорту",
            description = "Повертає історію призначень для конкретного транспорту"
    )
    @ApiResponse(responseCode = "200", description = "Список призначень отримано")
    public ResponseEntity<List<VehicleAssignmentResponseDto>> getByVehicle(
            @Parameter(description = "ID транспорту", required = true)
            @PathVariable Long vehicleId) {
        log.debug("REST request to get assignments for vehicle: {}", vehicleId);
        return ResponseEntity.ok(assignmentService.getByVehicle(vehicleId));
    }

    @GetMapping("/by-vehicle/{vehicleId}/active")
    @Operation(
            summary = "Отримати активне призначення для транспорту",
            description = "Повертає поточне активне призначення для транспорту"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Призначення знайдено"),
            @ApiResponse(responseCode = "404", description = "Активне призначення не знайдено")
    })
    public ResponseEntity<VehicleAssignmentResponseDto> getActiveByVehicle(
            @Parameter(description = "ID транспорту", required = true)
            @PathVariable Long vehicleId) {
        log.debug("REST request to get active assignment for vehicle: {}", vehicleId);
        return ResponseEntity.ok(assignmentService.getActiveByVehicle(vehicleId));
    }

    @GetMapping("/by-driver/{driverId}")
    @Operation(
            summary = "Отримати призначення водія",
            description = "Повертає історію призначень для конкретного водія"
    )
    @ApiResponse(responseCode = "200", description = "Список призначень отримано")
    public ResponseEntity<List<VehicleAssignmentResponseDto>> getByDriver(
            @Parameter(description = "ID водія", required = true)
            @PathVariable Long driverId) {
        log.debug("REST request to get assignments for driver: {}", driverId);
        return ResponseEntity.ok(assignmentService.getByDriver(driverId));
    }

    @GetMapping("/by-driver/{driverId}/active")
    @Operation(
            summary = "Отримати активні призначення водія",
            description = "Повертає поточні активні призначення водія"
    )
    @ApiResponse(responseCode = "200", description = "Список призначень отримано")
    public ResponseEntity<List<VehicleAssignmentResponseDto>> getActiveByDriver(
            @Parameter(description = "ID водія", required = true)
            @PathVariable Long driverId) {
        log.debug("REST request to get active assignments for driver: {}", driverId);
        return ResponseEntity.ok(assignmentService.getActiveByDriver(driverId));
    }

    @GetMapping("/by-category/{categoryId}")
    @Operation(
            summary = "Отримати активні призначення по категорії",
            description = "Повертає активні призначення для транспорту вказаної категорії"
    )
    @ApiResponse(responseCode = "200", description = "Список призначень отримано")
    public ResponseEntity<List<VehicleAssignmentResponseDto>> getActiveByCategory(
            @Parameter(description = "ID категорії транспорту", required = true)
            @PathVariable Long categoryId) {
        log.debug("REST request to get active assignments for category: {}", categoryId);
        return ResponseEntity.ok(assignmentService.getActiveByCategory(categoryId));
    }

    @GetMapping("/by-date-range")
    @Operation(
            summary = "Отримати призначення за період",
            description = "Повертає призначення створені в заданому діапазоні дат"
    )
    @ApiResponse(responseCode = "200", description = "Список призначень отримано")
    public ResponseEntity<List<VehicleAssignmentResponseDto>> getByDateRange(
            @Parameter(description = "Початкова дата (ISO формат)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "Кінцева дата (ISO формат)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        log.debug("REST request to get assignments between {} and {}", startDate, endDate);
        return ResponseEntity.ok(assignmentService.getByDateRange(startDate, endDate));
    }

    @PostMapping("/{id}/end")
    @Operation(
            summary = "Завершити призначення",
            description = """
                    Завершує активне призначення та фіксує кінцевий пробіг.
                    
                    **Дії:**
                    - Встановлює дату завершення
                    - Фіксує кінцевий пробіг
                    - Оновлює пробіг транспорту
                    - Деактивує призначення
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Призначення успішно завершено"),
            @ApiResponse(responseCode = "400", description = "Призначення вже завершено або невірний пробіг"),
            @ApiResponse(responseCode = "404", description = "Призначення не знайдено")
    })
    public ResponseEntity<VehicleAssignmentResponseDto> endAssignment(
            @Parameter(description = "ID призначення", required = true)
            @PathVariable Long id,
            @Valid @RequestBody VehicleAssignmentEndDto dto) {
        log.info("REST request to end assignment {}: {}", id, dto);
        return ResponseEntity.ok(assignmentService.endAssignment(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Видалити призначення",
            description = "Видаляє призначення з системи"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Призначення успішно видалено"),
            @ApiResponse(responseCode = "404", description = "Призначення не знайдено")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID призначення", required = true)
            @PathVariable Long id) {
        log.info("REST request to delete assignment: {}", id);
        assignmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
