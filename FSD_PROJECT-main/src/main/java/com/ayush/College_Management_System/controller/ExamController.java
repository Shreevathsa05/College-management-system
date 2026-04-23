package com.ayush.College_Management_System.controller;

import com.ayush.College_Management_System.dto.exam.*;
import com.ayush.College_Management_System.model.enums.ExamType;
import com.ayush.College_Management_System.service.ExamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService service;

    // ── CRUD ─────────────────────────────────────────────────────────────────

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
    public ResponseEntity<ExamResponseDTO> create(@Valid @RequestBody ExamRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ExamResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
    public ResponseEntity<ExamResponseDTO> update(
            @PathVariable Long id, @Valid @RequestBody ExamRequestDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    /**
     * DELETE is ADMIN-only. Service will throw if linked results exist.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ── Filter endpoints ──────────────────────────────────────────────────────

    /** GET /api/exams/upcoming — exams after today */
    @GetMapping("/upcoming")
    public ResponseEntity<List<ExamResponseDTO>> getUpcoming() {
        return ResponseEntity.ok(service.getUpcoming());
    }

    /** GET /api/exams/subject/{subjectId} */
    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<List<ExamResponseDTO>> getBySubject(@PathVariable Long subjectId) {
        return ResponseEntity.ok(service.getBySubject(subjectId));
    }

    /** GET /api/exams/type/{examType} */
    @GetMapping("/type/{examType}")
    public ResponseEntity<List<ExamResponseDTO>> getByType(@PathVariable ExamType examType) {
        return ResponseEntity.ok(service.getByExamType(examType));
    }

    /** GET /api/exams/session/{session} — e.g. /session/2025-26 */
    @GetMapping("/session/{session}")
    public ResponseEntity<List<ExamResponseDTO>> getBySession(@PathVariable String session) {
        return ResponseEntity.ok(service.getBySession(session));
    }

    /**
     * GET /api/exams/range?from=2026-04-01&to=2026-05-31
     * Returns exams scheduled between the two dates (inclusive).
     */
    @GetMapping("/range")
    public ResponseEntity<List<ExamResponseDTO>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(service.getByDateRange(from, to));
    }

    /**
     * GET /api/exams/classroom/{classroomId}
     * Lists all exams assigned to a room — useful for scheduling conflict checks.
     */
    @GetMapping("/classroom/{classroomId}")
    @PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
    public ResponseEntity<List<ExamResponseDTO>> getByClassroom(@PathVariable Long classroomId) {
        return ResponseEntity.ok(service.getByClassroom(classroomId));
    }
}