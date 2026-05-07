package com.ayush.College_Management_System.service.impl;

import com.ayush.College_Management_System.dto.student.StudentRequestDTO;
import com.ayush.College_Management_System.dto.student.StudentResponseDTO;
import com.ayush.College_Management_System.exception.ResourceNotFoundException;
import com.ayush.College_Management_System.model.*;
import com.ayush.College_Management_System.repository.*;
import com.ayush.College_Management_System.service.StudentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import com.ayush.College_Management_System.model.enums.StudentStatus;
import com.ayush.College_Management_System.specifications.StudentSpecifications;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepo;
    private final DepartmentRepository departmentRepo;
    private final CourseRepository courseRepo;

    @Override
    @Transactional
    public StudentResponseDTO createStudent(StudentRequestDTO dto) {

        log.info("Creating student with rollNo: {}", dto.getRollNo());

        Department dept = departmentRepo.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        Course course = courseRepo.findById(dto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        // RollNo check
        studentRepo.findByRollNo(dto.getRollNo()).ifPresent(s -> {
            throw new IllegalStateException("Roll number already exists");
        });

        // Enrollment check
        if (dto.getEnrollmentNumber() != null && !dto.getEnrollmentNumber().isBlank())  {
            studentRepo.findByEnrollmentNumber(dto.getEnrollmentNumber()).ifPresent(s -> {
                throw new IllegalStateException("Enrollment number already exists");
            });
        }

        Student student = new Student();
        mapToEntity(student, dto);
        student.setDepartment(dept);
        student.setCourse(course);

        Student saved = studentRepo.save(student);

        log.info("Student created successfully with id: {}", saved.getId());

        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponseDTO getStudentById(Long id) {
        log.info("Fetching student with id: {}", id);
        Student student = studentRepo.findById(id)
                .orElseThrow(() -> {
                    log.warn("Student not found with id: {}", id);
                    return new ResourceNotFoundException("Student not found");
                });
        return mapToResponse(student);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponseDTO> getAllStudents() {
        log.info("Fetching all students");
        return studentRepo.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public StudentResponseDTO updateStudent(Long id, StudentRequestDTO dto) {

        log.info("Updating student with id: {}", id);

        Student student = studentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        Department dept = departmentRepo.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        Course course = courseRepo.findById(dto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        // RollNo validation
        studentRepo.findByRollNo(dto.getRollNo()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new IllegalStateException("Roll number already exists");
            }
        });

        // Enrollment validation
        if (dto.getEnrollmentNumber() != null && !dto.getEnrollmentNumber().isBlank()) {
            studentRepo.findByEnrollmentNumber(dto.getEnrollmentNumber()).ifPresent(existing -> {
                if (!existing.getId().equals(id)) {
                    throw new IllegalStateException("Enrollment number already exists");
                }
            });
        }

        mapToEntity(student, dto);
        student.setDepartment(dept);
        student.setCourse(course);

        Student updated = studentRepo.save(student);

        log.info("Student updated successfully with id: {}", id);

        return mapToResponse(updated);
    }

    // PATCH — only update non-null fields
    @Override
    @Transactional
    public StudentResponseDTO patchStudent(Long id, StudentRequestDTO dto) {

        log.info("Patching student with id: {}", id);

        Student student = studentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        // RollNo validation
        if (dto.getRollNo() != null) {
            studentRepo.findByRollNo(dto.getRollNo()).ifPresent(existing -> {
                if (!existing.getId().equals(id)) {
                    throw new IllegalStateException("Roll number already exists");
                }
            });
            student.setRollNo(dto.getRollNo());
        }

        // Enrollment validation
        if (dto.getEnrollmentNumber() != null && !dto.getEnrollmentNumber().isBlank()) {
            studentRepo.findByEnrollmentNumber(dto.getEnrollmentNumber()).ifPresent(existing -> {
                if (!existing.getId().equals(id)) {
                    throw new IllegalStateException("Enrollment number already exists");
                }
            });
            student.setEnrollmentNumber(
                    (dto.getEnrollmentNumber() == null || dto.getEnrollmentNumber().isBlank())
                            ? null
                            : dto.getEnrollmentNumber()
            );
        }

        if (dto.getFirstName() != null) student.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) student.setLastName(dto.getLastName());
        if (dto.getGender() != null) student.setGender(dto.getGender());
        if (dto.getDob() != null) student.setDob(dto.getDob());
        if (dto.getEmail() != null) student.setEmail(dto.getEmail());
        if (dto.getPhone() != null) student.setPhone(dto.getPhone());
        if (dto.getCurrentSemester() != null) student.setCurrentSemester(dto.getCurrentSemester());
        if (dto.getAdmissionYear() != null) student.setAdmissionYear(dto.getAdmissionYear());
        if (dto.getStatus() != null) student.setStatus(dto.getStatus());
        if (dto.getAddress() != null) student.setAddress(dto.getAddress());
        if (dto.getAdmissionType() != null) student.setAdmissionType(dto.getAdmissionType());
        if (dto.getBloodGroup() != null) student.setBloodGroup(dto.getBloodGroup());
        if (dto.getProfileImageUrl() != null) student.setProfileImageUrl(dto.getProfileImageUrl());

        if (dto.getDepartmentId() != null) {
            Department dept = departmentRepo.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
            student.setDepartment(dept);
        }

        if (dto.getCourseId() != null) {
            Course course = courseRepo.findById(dto.getCourseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
            student.setCourse(course);
        }

        Student patched = studentRepo.save(student);
        log.info("Student patched successfully with id: {}", id);
        return mapToResponse(patched);
    }

    @Override
    @Transactional
    public void deleteStudent(Long id) {
        log.info("Deleting student with id: {}", id);
        if (!studentRepo.existsById(id)) {
            log.warn("Student not found with id: {}", id);
            throw new ResourceNotFoundException("Student not found");
        }
        studentRepo.deleteById(id);
        log.info("Student deleted successfully with id: {}", id);
    }

    private void mapToEntity(Student student, StudentRequestDTO dto) {
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setGender(dto.getGender());
        student.setDob(dto.getDob());
        student.setRollNo(dto.getRollNo());
        student.setEmail(dto.getEmail());
        student.setPhone(dto.getPhone());
        student.setCurrentSemester(dto.getCurrentSemester());
        student.setAdmissionYear(dto.getAdmissionYear());
        student.setStatus(dto.getStatus());
        student.setAddress(dto.getAddress());
        student.setEnrollmentNumber(
                (dto.getEnrollmentNumber() == null || dto.getEnrollmentNumber().isBlank())
                        ? null
                        : dto.getEnrollmentNumber()
        );
        student.setAdmissionType(dto.getAdmissionType());
        student.setBloodGroup(dto.getBloodGroup());
        student.setProfileImageUrl(dto.getProfileImageUrl());
    }

    private StudentResponseDTO mapToResponse(Student student) {
        StudentResponseDTO dto = new StudentResponseDTO();

        dto.setId(student.getId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setGender(student.getGender());
        dto.setDob(student.getDob());
        dto.setRollNo(student.getRollNo());
        dto.setEmail(student.getEmail());
        dto.setPhone(student.getPhone());
        dto.setCurrentSemester(student.getCurrentSemester());
        dto.setAdmissionYear(student.getAdmissionYear());
        dto.setStatus(student.getStatus());
        dto.setAddress(student.getAddress());
        dto.setEnrollmentNumber(student.getEnrollmentNumber());
        dto.setAdmissionType(student.getAdmissionType());
        dto.setBloodGroup(student.getBloodGroup());
        dto.setProfileImageUrl(student.getProfileImageUrl());
        dto.setIsActive(student.getIsActive());

        // ✅ null-safe mapping
        dto.setDepartmentName(
                student.getDepartment() != null
                        ? student.getDepartment().getName()
                        : null
        );

        dto.setCourseName(
                student.getCourse() != null
                        ? student.getCourse().getCourseTitle()
                        : null
        );

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponseDTO> searchStudents(String keyword) {
        log.info("Searching students with keyword: {}", keyword);

        return studentRepo.searchStudents(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponseDTO> filterStudents(
            Long departmentId,
            Long courseId,
            StudentStatus status,
            Integer semester,
            Integer passoutYear
    ) {
        log.info("Filtering students with departmentId={}, courseId={}, status={}, semester={}, passoutYear={}",
                departmentId, courseId, status, semester, passoutYear);

        return studentRepo.findAll(
                       StudentSpecifications.filterStudents(
                                departmentId,
                                courseId,
                                status,
                                semester,
                                passoutYear
                        )
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

}