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
import ua.edu.viti.military.dto.request.VehicleCreateDTO;
import ua.edu.viti.military.dto.request.VehicleUpdateDTO;
import ua.edu.viti.military.dto.response.VehicleResponseDTO;
import ua.edu.viti.military.entity.VehicleStatus;
import ua.edu.viti.military.exception.ErrorResponse;
import ua.edu.viti.military.service.VehicleService;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Vehicles", description = "API для управління військовим транспортом")
public class VehicleController {

    private final VehicleService vehicleService;

    @Operation(
            summary = "Створити новий транспортний засіб",
            description = "Реєструє новий транспортний засіб з унікальним реєстраційним номером"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Транспорт успішно створено",
                    content = @Content(schema = @Schema(implementation = VehicleResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Невалідні дані або бізнес-логіка",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Категорію або водія не знайдено",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Транспорт з таким номером вже існує",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<VehicleResponseDTO> create(
            @Valid @RequestBody VehicleCreateDTO dto) {
        log.info("POST /api/vehicles - Creating new vehicle with registration: {}", dto.getRegistrationNumber());
        VehicleResponseDTO created = vehicleService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(
            summary = "Отримати транспорт за ID",
            description = "Повертає детальну інформацію про транспортний засіб за його унікальним ідентифікатором"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Транспорт знайдено",
                    content = @Content(schema = @Schema(implementation = VehicleResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Транспорт не знайдено",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponseDTO> getById(
            @Parameter(description = "ID транспорту") @PathVariable Long id) {
        log.info("GET /api/vehicles/{} - Fetching vehicle", id);
        return ResponseEntity.ok(vehicleService.getById(id));
    }

    @Operation(
            summary = "Отримати всі транспортні засоби",
            description = "Повертає список всіх транспортних засобів з можливістю фільтрації за статусом та категорією"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список транспорту",
                    content = @Content(schema = @Schema(implementation = VehicleResponseDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<VehicleResponseDTO>> getAll(
            @Parameter(description = "Фільтр за статусом транспорту") 
            @RequestParam(required = false) VehicleStatus status,
            @Parameter(description = "Фільтр за ID категорії") 
            @RequestParam(required = false) Long categoryId) {
        log.info("GET /api/vehicles - Fetching all vehicles with status: {}, categoryId: {}", status, categoryId);
        return ResponseEntity.ok(vehicleService.getAll(status, categoryId));
    }

    @Operation(
            summary = "Оновити транспортний засіб",
            description = "Оновлює інформацію про транспортний засіб за його ID. Всі поля опціональні."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Транспорт успішно оновлено",
                    content = @Content(schema = @Schema(implementation = VehicleResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Невалідні дані або бізнес-логіка",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Транспорт, категорію або водія не знайдено",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Транспорт з таким номером вже існує",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<VehicleResponseDTO> update(
            @Parameter(description = "ID транспорту") @PathVariable Long id,
            @Valid @RequestBody VehicleUpdateDTO dto) {
        log.info("PUT /api/vehicles/{} - Updating vehicle", id);
        return ResponseEntity.ok(vehicleService.update(id, dto));
    }

    @Operation(
            summary = "Видалити транспортний засіб",
            description = "Видаляє транспортний засіб за його ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Транспорт успішно видалено"),
            @ApiResponse(responseCode = "404", description = "Транспорт не знайдено",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID транспорту") @PathVariable Long id) {
        log.info("DELETE /api/vehicles/{} - Deleting vehicle", id);
        vehicleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Отримати транспорт що потребує ТО",
            description = "Повертає список транспортних засобів, які потребують технічного обслуговування " +
                    "(пробіг з останнього ТО >= інтервалу ТО)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список транспорту що потребує ТО",
                    content = @Content(schema = @Schema(implementation = VehicleResponseDTO.class)))
    })
    @GetMapping("/requiring-maintenance")
    public ResponseEntity<List<VehicleResponseDTO>> findRequiringMaintenance() {
        log.info("GET /api/vehicles/requiring-maintenance - Finding vehicles requiring maintenance");
        return ResponseEntity.ok(vehicleService.findRequiringMaintenance());
    }

    @Operation(
            summary = "Отримати транспорт за водієм",
            description = "Повертає список транспортних засобів, закріплених за конкретним водієм"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список транспорту водія",
                    content = @Content(schema = @Schema(implementation = VehicleResponseDTO.class)))
    })
    @GetMapping("/by-driver/{driverId}")
    public ResponseEntity<List<VehicleResponseDTO>> findByDriver(
            @Parameter(description = "ID водія") @PathVariable Long driverId) {
        log.info("GET /api/vehicles/by-driver/{} - Finding vehicles by driver", driverId);
        return ResponseEntity.ok(vehicleService.findByDriverId(driverId));
    }
}
