package com.ayush.College_Management_System.controller;

import com.ayush.College_Management_System.dto.student.*;
import com.ayush.College_Management_System.model.enums.StudentStatus;
import com.ayush.College_Management_System.security.SecurityUserAccessor;
import com.ayush.College_Management_System.service.StudentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@Slf4j
public class StudentController {

    private final StudentService studentService;
    private final SecurityUserAccessor securityUserAccessor;

    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Get current logged-in student profile")
    public ResponseEntity<StudentResponseDTO> getMyProfile() {
        Long linkedId = securityUserAccessor.getCurrentUser().getLinkedId();
        if (linkedId == null) {
            throw new AccessDeniedException("Student account must have linkedId set to the student record id");
        }
        return ResponseEntity.ok(studentService.getStudentById(linkedId));
    }

    // ✅ CREATE
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new student")
    public ResponseEntity<StudentResponseDTO> createStudent(
        @Valid @RequestBody StudentRequestDTO dto) {
        log.info("API: Create Student");
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.createStudent(dto));
    }

    // ✅ GET BY ID
    @GetMapping("/{id}")
    @Operation(summary = "Get student by ID")
    public ResponseEntity<StudentResponseDTO> getStudent(
        @Parameter(description = "Student ID", example = "1") 
        @PathVariable Long id) {
        log.info("API: Get Student by id {}", id);
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    // ✅ GET ALL
    @GetMapping
    @Operation(summary = "Get all students")
    public ResponseEntity<List<StudentResponseDTO>> getAllStudents() {
        log.info("API: Get all students");
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    // ✅ UPDATE (PUT — full update)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update student by ID")
    public ResponseEntity<StudentResponseDTO> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentRequestDTO dto) {

        log.info("API: Update Student {}", id);
        return ResponseEntity.ok(studentService.updateStudent(id, dto));
    }

    // ✅ PARTIAL UPDATE (PATCH — only updates non-null fields)
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Partially update student by ID")
    public ResponseEntity<StudentResponseDTO> patchStudent(
            @PathVariable Long id,
            @RequestBody StudentRequestDTO dto) {

        log.info("API: Patch Student {}", id);
        return ResponseEntity.ok(studentService.patchStudent(id, dto));
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete student by ID")
    public ResponseEntity<Void> deleteStudent(
        @Parameter(description = "Student ID", example = "1")
        @PathVariable Long id) {
        log.info("API: Delete Student {}", id);
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search students by keyword")
    public ResponseEntity<List<StudentResponseDTO>> searchStudents(
            @Parameter(description = "Keyword to search for", example = "John")
            @RequestParam String keyword) {
        log.info("API: Search students with keyword {}", keyword);
        return ResponseEntity.ok(studentService.searchStudents(keyword));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<StudentResponseDTO>> filterStudents(
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) StudentStatus status,
            @RequestParam(required = false) Integer semester,
            @RequestParam(required = false) Integer passoutYear
    ) {

        log.info("API: Filter students");

        return ResponseEntity.ok(
                studentService.filterStudents(
                        departmentId,
                        courseId,
                        status,
                        semester,
                        passoutYear
                )
        );
    }
}