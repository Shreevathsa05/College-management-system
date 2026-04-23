package com.ayush.College_Management_System.controller;

import com.ayush.College_Management_System.dto.classroom.*;
import com.ayush.College_Management_System.model.enums.ClassroomType;
import com.ayush.College_Management_System.service.ClassroomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classrooms")
@RequiredArgsConstructor
public class ClassroomController {

    private final ClassroomService service;

    // ── CRUD ─────────────────────────────────────────────────────────────────

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClassroomResponseDTO> create(
            @Valid @RequestBody ClassroomRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassroomResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ClassroomResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClassroomResponseDTO> update(
            @PathVariable Long id, @Valid @RequestBody ClassroomRequestDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ── Availability toggle ───────────────────────────────────────────────────

    /**
     * PATCH /api/classrooms/{id}/availability?value=false
     * Toggles availability without a full PUT update.
     */
    @PatchMapping("/{id}/availability")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClassroomResponseDTO> toggleAvailability(
            @PathVariable Long id,
            @RequestParam Boolean value) {
        return ResponseEntity.ok(service.toggleAvailability(id, value));
    }

    // ── Filter endpoints ──────────────────────────────────────────────────────

    /** GET /api/classrooms/available */
    @GetMapping("/available")
    public ResponseEntity<List<ClassroomResponseDTO>> getAvailable() {
        return ResponseEntity.ok(service.getAvailable());
    }

    /** GET /api/classrooms/type/{type} */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<ClassroomResponseDTO>> getByType(
            @PathVariable ClassroomType type) {
        return ResponseEntity.ok(service.getByType(type));
    }

    /** GET /api/classrooms/department/{deptId} */
    @GetMapping("/department/{deptId}")
    public ResponseEntity<List<ClassroomResponseDTO>> getByDepartment(
            @PathVariable Long deptId) {
        return ResponseEntity.ok(service.getByDepartment(deptId));
    }
}