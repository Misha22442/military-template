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
import ua.edu.viti.military.dto.request.DriverCreateDTO;
import ua.edu.viti.military.dto.request.DriverUpdateDTO;
import ua.edu.viti.military.dto.response.DriverResponseDTO;
import ua.edu.viti.military.exception.ErrorResponse;
import ua.edu.viti.military.service.DriverService;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Drivers", description = "API для управління водіями військового транспорту")
public class DriverController {

    private final DriverService driverService;

    @Operation(
            summary = "Створити нового водія",
            description = "Реєструє нового водія з унікальним військовим ID та номером посвідчення"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Водія успішно створено",
                    content = @Content(schema = @Schema(implementation = DriverResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Невалідні дані",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Водій з таким військовим ID або номером посвідчення вже існує",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<DriverResponseDTO> create(
            @Valid @RequestBody DriverCreateDTO dto) {
        log.info("POST /api/drivers - Creating new driver with military ID: {}", dto.getMilitaryId());
        DriverResponseDTO created = driverService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(
            summary = "Отримати водія за ID",
            description = "Повертає детальну інформацію про водія за його унікальним ідентифікатором"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Водія знайдено",
                    content = @Content(schema = @Schema(implementation = DriverResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Водія не знайдено",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<DriverResponseDTO> getById(
            @Parameter(description = "ID водія") @PathVariable Long id) {
        log.info("GET /api/drivers/{} - Fetching driver", id);
        return ResponseEntity.ok(driverService.getById(id));
    }

    @Operation(
            summary = "Отримати всіх водіїв",
            description = "Повертає список всіх водіїв з можливістю фільтрації за активністю"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список водіїв",
                    content = @Content(schema = @Schema(implementation = DriverResponseDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<DriverResponseDTO>> getAll(
            @Parameter(description = "Фільтр за активністю водія") 
            @RequestParam(required = false) Boolean isActive) {
        log.info("GET /api/drivers - Fetching all drivers with isActive: {}", isActive);
        return ResponseEntity.ok(driverService.getAll(isActive));
    }

    @Operation(
            summary = "Оновити водія",
            description = "Оновлює інформацію про водія за його ID. Всі поля опціональні."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Водія успішно оновлено",
                    content = @Content(schema = @Schema(implementation = DriverResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Невалідні дані",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Водія не знайдено",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Водій з таким номером посвідчення вже існує",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<DriverResponseDTO> update(
            @Parameter(description = "ID водія") @PathVariable Long id,
            @Valid @RequestBody DriverUpdateDTO dto) {
        log.info("PUT /api/drivers/{} - Updating driver", id);
        return ResponseEntity.ok(driverService.update(id, dto));
    }

    @Operation(
            summary = "Видалити водія",
            description = "Видаляє водія за його ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Водія успішно видалено"),
            @ApiResponse(responseCode = "404", description = "Водія не знайдено",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID водія") @PathVariable Long id) {
        log.info("DELETE /api/drivers/{} - Deleting driver", id);
        driverService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Знайти водіїв з простроченим посвідченням",
            description = "Повертає список водіїв, у яких термін дії посвідчення вже закінчився"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список водіїв з простроченим посвідченням",
                    content = @Content(schema = @Schema(implementation = DriverResponseDTO.class)))
    })
    @GetMapping("/expired-license")
    public ResponseEntity<List<DriverResponseDTO>> findWithExpiredLicense() {
        log.info("GET /api/drivers/expired-license - Finding drivers with expired license");
        return ResponseEntity.ok(driverService.findWithExpiredLicense());
    }

    @Operation(
            summary = "Знайти водіїв з посвідченням що закінчується",
            description = "Повертає список водіїв, у яких термін дії посвідчення закінчується протягом вказаного періоду"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список водіїв",
                    content = @Content(schema = @Schema(implementation = DriverResponseDTO.class)))
    })
    @GetMapping("/expiring-license")
    public ResponseEntity<List<DriverResponseDTO>> findWithExpiringLicense(
            @Parameter(description = "Кількість днів до закінчення терміну дії посвідчення") 
            @RequestParam(defaultValue = "30") int days) {
        log.info("GET /api/drivers/expiring-license - Finding drivers with license expiring in {} days", days);
        return ResponseEntity.ok(driverService.findWithExpiringLicense(days));
    }
}
