package com.ayush.College_Management_System.service.impl;

import com.ayush.College_Management_System.dto.hostel.*;
import com.ayush.College_Management_System.exception.ResourceNotFoundException;
import com.ayush.College_Management_System.model.*;
import com.ayush.College_Management_System.repository.*;
import com.ayush.College_Management_System.service.HostelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class HostelServiceImpl implements HostelService {
    private final HostelRepository repo;
    private final StudentRepository studentRepo;

    @Override @Transactional
    public HostelResponseDTO create(HostelRequestDTO dto) {
        Hostel e = new Hostel(); map(e, dto);
        if (dto.getStudentId() != null) e.setStudent(studentRepo.findById(dto.getStudentId()).orElseThrow(() -> new ResourceNotFoundException("Student not found")));
        return toResponse(repo.save(e));
    }

    @Override @Transactional(readOnly = true)
    public HostelResponseDTO getById(Long id) {
        return toResponse(repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hostel not found")));
    }

    @Override @Transactional(readOnly = true)
    public List<HostelResponseDTO> getAll() { return repo.findAll().stream().map(this::toResponse).collect(Collectors.toList()); }

    @Override @Transactional
    public HostelResponseDTO update(Long id, HostelRequestDTO dto) {
        Hostel e = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hostel not found"));
        map(e, dto);
        if (dto.getStudentId() != null) e.setStudent(studentRepo.findById(dto.getStudentId()).orElseThrow(() -> new ResourceNotFoundException("Student not found")));
        return toResponse(repo.save(e));
    }

    @Override @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Hostel not found");
        repo.deleteById(id);
    }

    private void map(Hostel e, HostelRequestDTO d) {
        e.setHostelName(d.getHostelName()); e.setRoomNumber(d.getRoomNumber());
        e.setFloorNumber(d.getFloorNumber()); e.setBlockName(d.getBlockName());
        e.setRoomType(d.getRoomType()); e.setMessFee(d.getMessFee()); e.setHostelFee(d.getHostelFee());
        if (d.getIsOccupied() != null) e.setIsOccupied(d.getIsOccupied());
    }

    private HostelResponseDTO toResponse(Hostel e) {
        HostelResponseDTO d = new HostelResponseDTO();
        d.setId(e.getId()); d.setHostelName(e.getHostelName()); d.setRoomNumber(e.getRoomNumber());
        d.setFloorNumber(e.getFloorNumber()); d.setBlockName(e.getBlockName());
        d.setRoomType(e.getRoomType()); d.setMessFee(e.getMessFee()); d.setHostelFee(e.getHostelFee());
        d.setIsOccupied(e.getIsOccupied());
        d.setStudentName(e.getStudent() != null ? e.getStudent().getFirstName() : null);
        d.setStudentRollNo(e.getStudent() != null ? e.getStudent().getRollNo() : null);
        return d;
    }
}