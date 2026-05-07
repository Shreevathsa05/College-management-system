package com.ayush.College_Management_System.controller;
import com.ayush.College_Management_System.dto.student.*;
import com.ayush.College_Management_System.security.SecurityUserAccessor;
import com.ayush.College_Management_System.service.StudentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.ayush.College_Management_System.model.enums.AdmissionType;
import com.ayush.College_Management_System.model.enums.StudentStatus;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Students", description = "Endpoints for managing student records")
public class StudentController {

    private final StudentService studentService;
    private final SecurityUserAccessor securityUserAccessor;

    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Get the authenticated student's profile")
    public ResponseEntity<StudentResponseDTO> getMyProfile() {
        log.info("Fetching profile for authenticated student");
        Long linkedId = securityUserAccessor.getCurrentUser().getLinkedId();
        if (linkedId == null) {
            throw new AccessDeniedException("Student account must have linkedId set to the student record id");
        }
        return ResponseEntity.ok(studentService.getStudentById(linkedId));
    }

    // CREATE
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new student record")
    public ResponseEntity<StudentResponseDTO> createStudent(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Student request payload") @Valid @RequestBody StudentRequestDTO dto) {
        log.info("API: Create Student");
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.createStudent(dto));
    }

    // GET BY ID
    @GetMapping("/{id}")
    @Operation(summary = "Get a student by ID")
    public ResponseEntity<StudentResponseDTO> getStudent(@PathVariable @Parameter(description = "ID of the student to retrieve") Long id) {
        log.info("API: Get Student by id {}", id);
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    // GET ALL
    @GetMapping
    @Operation(summary = "Get all students")
    public ResponseEntity<List<StudentResponseDTO>> getAllStudents() {
        log.info("API: Get all students");
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    // UPDATE (PUT — full update)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update an existing student record")
    public ResponseEntity<StudentResponseDTO> updateStudent(
            @PathVariable @Parameter(description = "ID of the student to update") Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated student request payload") @Valid @RequestBody StudentRequestDTO dto) {

        log.info("API: Update Student {}", id);
        return ResponseEntity.ok(studentService.updateStudent(id, dto));
    }

    // PARTIAL UPDATE (PATCH — only updates non-null fields)
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Partially update a student record")
    public ResponseEntity<StudentResponseDTO> patchStudent(
            @PathVariable @Parameter(description = "ID of the student to patch") Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Student fields to patch") @RequestBody StudentRequestDTO dto) {

        log.info("API: Patch Student {}", id);
        return ResponseEntity.ok(studentService.patchStudent(id, dto));
    }

    // DELETE
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a student by ID")
    public ResponseEntity<Void> deleteStudent(@PathVariable @Parameter(description = "ID of the student to delete") Long id) {
        log.info("API: Delete Student {}", id);
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search students by keyword")
    public ResponseEntity<List<StudentResponseDTO>> searchStudents(
            @RequestParam @Parameter(description = "Search keyword for student name, roll number, or other fields") String keyword) {

        log.info("API: Search students with keyword {}", keyword);
        return ResponseEntity.ok(studentService.searchStudents(keyword));
    }

    @GetMapping("/filter")
    @Operation(summary = "Filter students by department, course, status, semester, or passout year")
    public ResponseEntity<List<StudentResponseDTO>> filterStudents(
            @RequestParam(required = false) @Parameter(description = "Department ID to filter by") Long departmentId,
            @RequestParam(required = false) @Parameter(description = "Course ID to filter by") Long courseId,
            @RequestParam(required = false) @Parameter(description = "Student status to filter by") StudentStatus status,
            @RequestParam(required = false) @Parameter(description = "Semester number to filter by") Integer semester,
            @RequestParam(required = false) @Parameter(description = "Passout year to filter by") Integer passoutYear
    ) {

        log.info("API: Filter students with departmentId={}, courseId={}, status={}, semester={}, passoutYear={}",
                departmentId, courseId, status, semester, passoutYear);

        return ResponseEntity.ok(studentService.filterStudents(
                departmentId,
                courseId,
                status,
                semester,
                passoutYear
        ));
    }
}