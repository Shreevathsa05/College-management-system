package com.ayush.College_Management_System.controller;

import com.ayush.College_Management_System.dto.infrastructure.*;
import com.ayush.College_Management_System.model.enums.InfrastructureStatus;
import com.ayush.College_Management_System.model.enums.InfrastructureType;
import com.ayush.College_Management_System.service.InfrastructureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/infrastructure")
@RequiredArgsConstructor
public class InfrastructureController {

    private final InfrastructureService service;

    // ── CRUD ─────────────────────────────────────────────────────────────────

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InfrastructureResponseDTO> create(
            @Valid @RequestBody InfrastructureRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InfrastructureResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<InfrastructureResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InfrastructureResponseDTO> update(
            @PathVariable Long id, @Valid @RequestBody InfrastructureRequestDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ── Status patch ──────────────────────────────────────────────────────────

    /**
     * PATCH /api/infrastructure/{id}/status?value=UNDER_MAINTENANCE
     * Updates only the status field without a full PUT.
     */
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InfrastructureResponseDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam InfrastructureStatus value) {
        return ResponseEntity.ok(service.updateStatus(id, value));
    }

    // ── Filter endpoints ──────────────────────────────────────────────────────

    /** GET /api/infrastructure/department/{deptId} */
    @GetMapping("/department/{deptId}")
    public ResponseEntity<List<InfrastructureResponseDTO>> getByDepartment(
            @PathVariable Long deptId) {
        return ResponseEntity.ok(service.getByDepartment(deptId));
    }

    /** GET /api/infrastructure/status/{status} */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<InfrastructureResponseDTO>> getByStatus(
            @PathVariable InfrastructureStatus status) {
        return ResponseEntity.ok(service.getByStatus(status));
    }

    /** GET /api/infrastructure/type/{type} */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<InfrastructureResponseDTO>> getByType(
            @PathVariable InfrastructureType type) {
        return ResponseEntity.ok(service.getByType(type));
    }

    /** GET /api/infrastructure/block/{block} */
    @GetMapping("/block/{block}")
    public ResponseEntity<List<InfrastructureResponseDTO>> getByBlock(
            @PathVariable String block) {
        return ResponseEntity.ok(service.getByBlock(block));
    }
}