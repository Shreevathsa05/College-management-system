package com.ayush.College_Management_System.service;

import com.ayush.College_Management_System.dto.exam.*;
import com.ayush.College_Management_System.model.enums.ExamType;

import java.time.LocalDate;
import java.util.List;

public interface ExamService {
    ExamResponseDTO create(ExamRequestDTO dto);
    ExamResponseDTO getById(Long id);
    List<ExamResponseDTO> getAll();
    ExamResponseDTO update(Long id, ExamRequestDTO dto);
    void delete(Long id);

    // Filter operations
    List<ExamResponseDTO> getBySubject(Long subjectId);
    List<ExamResponseDTO> getByExamType(ExamType examType);
    List<ExamResponseDTO> getBySession(String session);
    List<ExamResponseDTO> getByDateRange(LocalDate from, LocalDate to);
    List<ExamResponseDTO> getUpcoming();

    // Conflict check
    List<ExamResponseDTO> getByClassroom(Long classroomId);
}