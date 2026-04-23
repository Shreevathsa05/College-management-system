package com.ayush.College_Management_System.service;

import com.ayush.College_Management_System.dto.infrastructure.*;
import com.ayush.College_Management_System.model.enums.InfrastructureStatus;
import com.ayush.College_Management_System.model.enums.InfrastructureType;

import java.util.List;

public interface InfrastructureService {
    InfrastructureResponseDTO create(InfrastructureRequestDTO dto);
    InfrastructureResponseDTO getById(Long id);
    List<InfrastructureResponseDTO> getAll();
    InfrastructureResponseDTO update(Long id, InfrastructureRequestDTO dto);
    void delete(Long id);

    // Filter operations
    List<InfrastructureResponseDTO> getByDepartment(Long departmentId);
    List<InfrastructureResponseDTO> getByStatus(InfrastructureStatus status);
    List<InfrastructureResponseDTO> getByType(InfrastructureType type);
    List<InfrastructureResponseDTO> getByBlock(String block);

    // Status update shortcut
    InfrastructureResponseDTO updateStatus(Long id, InfrastructureStatus status);
}