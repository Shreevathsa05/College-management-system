package com.ayush.College_Management_System.service;

import com.ayush.College_Management_System.dto.classroom.*;
import com.ayush.College_Management_System.model.enums.ClassroomType;

import java.util.List;

public interface ClassroomService {
    ClassroomResponseDTO create(ClassroomRequestDTO dto);
    ClassroomResponseDTO getById(Long id);
    List<ClassroomResponseDTO> getAll();
    ClassroomResponseDTO update(Long id, ClassroomRequestDTO dto);
    void delete(Long id);

    // Filter operations
    List<ClassroomResponseDTO> getByDepartment(Long departmentId);
    List<ClassroomResponseDTO> getByType(ClassroomType type);
    List<ClassroomResponseDTO> getAvailable();

    // Availability toggle
    ClassroomResponseDTO toggleAvailability(Long id, Boolean isAvailable);
}