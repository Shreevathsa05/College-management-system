package com.ayush.College_Management_System.service;
import com.ayush.College_Management_System.dto.hostel.*;
import java.util.List;
public interface HostelService {
    HostelResponseDTO create(HostelRequestDTO dto);
    HostelResponseDTO getById(Long id);
    List<HostelResponseDTO> getAll();
    HostelResponseDTO update(Long id, HostelRequestDTO dto);
    void delete(Long id);
}