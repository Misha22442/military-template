package ua.edu.viti.military.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.edu.viti.military.dto.request.VehicleMovementRequestDto;
import ua.edu.viti.military.dto.response.VehicleMovementResponseDto;
import ua.edu.viti.military.service.VehicleMovementLogService;

import java.util.List;

/**
 * Controller for vehicle movement log operations.
 */
@RestController
@RequestMapping("/api/vehicle-movements")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Vehicle Movements", description = "API для операцій з транспортом")
public class VehicleMovementLogController {
    
    private final VehicleMovementLogService movementLogService;
    
    @PostMapping("/assign-driver")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    @Operation(summary = "Призначити водія до транспорту")
    public ResponseEntity<VehicleMovementResponseDto> assignDriver(
            @Valid @RequestBody VehicleMovementRequestDto dto) {
        
        VehicleMovementResponseDto response = movementLogService.assignDriver(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PostMapping("/unassign-driver/{vehicleId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    @Operation(summary = "Відкликати водія від транспорту")
    public ResponseEntity<VehicleMovementResponseDto> unassignDriver(
            @Parameter(description = "ID транспорту") @PathVariable Long vehicleId,
            @Valid @RequestBody VehicleMovementRequestDto dto) {
        
        dto.setVehicleId(vehicleId);
        VehicleMovementResponseDto response = movementLogService.unassignDriver(vehicleId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PostMapping("/send-to-maintenance")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    @Operation(summary = "Відправити транспорт на ТО")
    public ResponseEntity<VehicleMovementResponseDto> sendToMaintenance(
            @Valid @RequestBody VehicleMovementRequestDto dto) {
        
        VehicleMovementResponseDto response = movementLogService.sendToMaintenance(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PostMapping("/return-from-maintenance/{vehicleId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    @Operation(summary = "Повернути транспорт з ТО")
    public ResponseEntity<VehicleMovementResponseDto> returnFromMaintenance(
            @Parameter(description = "ID транспорту") @PathVariable Long vehicleId,
            @Valid @RequestBody VehicleMovementRequestDto dto) {
        
        dto.setVehicleId(vehicleId);
        VehicleMovementResponseDto response = movementLogService.returnFromMaintenance(vehicleId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/vehicle/{vehicleId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Історія операцій для транспорту")
    public ResponseEntity<List<VehicleMovementResponseDto>> getVehicleHistory(
            @Parameter(description = "ID транспорту") @PathVariable Long vehicleId) {
        
        List<VehicleMovementResponseDto> history = movementLogService.getVehicleHistory(vehicleId);
        return ResponseEntity.ok(history);
    }
    
    @GetMapping("/driver/{driverId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Історія операцій для водія")
    public ResponseEntity<List<VehicleMovementResponseDto>> getDriverHistory(
            @Parameter(description = "ID водія") @PathVariable Long driverId) {
        
        List<VehicleMovementResponseDto> history = movementLogService.getDriverHistory(driverId);
        return ResponseEntity.ok(history);
    }
    
    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Остання активність")
    public ResponseEntity<List<VehicleMovementResponseDto>> getRecentActivity(
            @RequestParam(defaultValue = "20") int limit) {
        
        List<VehicleMovementResponseDto> activity = movementLogService.getRecentActivity(limit);
        return ResponseEntity.ok(activity);
    }
}
