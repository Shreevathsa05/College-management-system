package com.ayush.College_Management_System.controller;
import com.ayush.College_Management_System.dto.student.*;
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

import com.ayush.College_Management_System.model.enums.AdmissionType;
import com.ayush.College_Management_System.model.enums.StudentStatus;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@Slf4j
public class StudentController {

    private final StudentService studentService;
    private final SecurityUserAccessor securityUserAccessor;

    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<StudentResponseDTO> getMyProfile() {
        Long linkedId = securityUserAccessor.getCurrentUser().getLinkedId();
        if (linkedId == null) {
            throw new AccessDeniedException("Student account must have linkedId set to the student record id");
        }
        return ResponseEntity.ok(studentService.getStudentById(linkedId));
    }

    // CREATE
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StudentResponseDTO> createStudent(@Valid @RequestBody StudentRequestDTO dto) {
        log.info("API: Create Student");
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.createStudent(dto));
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getStudent(@PathVariable Long id) {
        log.info("API: Get Student by id {}", id);
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<StudentResponseDTO>> getAllStudents() {
        log.info("API: Get all students");
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    // UPDATE (PUT — full update)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StudentResponseDTO> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentRequestDTO dto) {

        log.info("API: Update Student {}", id);
        return ResponseEntity.ok(studentService.updateStudent(id, dto));
    }

    // PARTIAL UPDATE (PATCH — only updates non-null fields)
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StudentResponseDTO> patchStudent(
            @PathVariable Long id,
            @RequestBody StudentRequestDTO dto) {

        log.info("API: Patch Student {}", id);
        return ResponseEntity.ok(studentService.patchStudent(id, dto));
    }

    // DELETE
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        log.info("API: Delete Student {}", id);
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<StudentResponseDTO>> searchStudents(
            @RequestParam String keyword) {

        log.info("API: Search students with keyword {}", keyword);
        return ResponseEntity.ok(studentService.searchStudents(keyword));
    }
}