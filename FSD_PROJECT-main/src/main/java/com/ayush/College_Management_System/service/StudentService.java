package com.ayush.College_Management_System.service;

import java.util.List;

import com.ayush.College_Management_System.dto.student.StudentRequestDTO;
import com.ayush.College_Management_System.dto.student.StudentResponseDTO;
import com.ayush.College_Management_System.model.enums.StudentStatus;

public interface StudentService {

    StudentResponseDTO createStudent(StudentRequestDTO dto);

    StudentResponseDTO getStudentById(Long id);

    List<StudentResponseDTO> getAllStudents();

    StudentResponseDTO updateStudent(Long id, StudentRequestDTO dto);

    StudentResponseDTO patchStudent(Long id, StudentRequestDTO dto);
    
    List<StudentResponseDTO> searchStudents(String keyword);

    void deleteStudent(Long id);

    List<StudentResponseDTO> filterStudents(
        Long departmentId,
        Long courseId,
        StudentStatus status,
        Integer semester,
        Integer passoutYear
);
}