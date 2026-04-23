package com.ayush.College_Management_System.service.impl;

import com.ayush.College_Management_System.dto.infrastructure.*;
import com.ayush.College_Management_System.exception.ResourceNotFoundException;
import com.ayush.College_Management_System.model.*;
import com.ayush.College_Management_System.model.enums.InfrastructureStatus;
import com.ayush.College_Management_System.model.enums.InfrastructureType;
import com.ayush.College_Management_System.repository.*;
import com.ayush.College_Management_System.service.InfrastructureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InfrastructureServiceImpl implements InfrastructureService {

    private final InfrastructureRepository repo;
    private final DepartmentRepository deptRepo;

    // ── CRUD ─────────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public InfrastructureResponseDTO create(InfrastructureRequestDTO dto) {
        Department dept = deptRepo.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        Infrastructure e = new Infrastructure();
        map(e, dto);
        e.setDepartment(dept);
        return toResponse(repo.save(e));
    }

    @Override
    @Transactional(readOnly = true)
    public InfrastructureResponseDTO getById(Long id) {
        return toResponse(find(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InfrastructureResponseDTO> getAll() {
        return repo.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public InfrastructureResponseDTO update(Long id, InfrastructureRequestDTO dto) {
        Infrastructure e = find(id);
        Department dept = deptRepo.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        map(e, dto);
        e.setDepartment(dept);
        return toResponse(repo.save(e));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Infrastructure not found");
        repo.deleteById(id);
    }

    // ── Filters ───────────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<InfrastructureResponseDTO> getByDepartment(Long departmentId) {
        return repo.findByDepartmentId(departmentId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InfrastructureResponseDTO> getByStatus(InfrastructureStatus status) {
        return repo.findByStatus(status).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InfrastructureResponseDTO> getByType(InfrastructureType type) {
        return repo.findByType(type).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InfrastructureResponseDTO> getByBlock(String block) {
        return repo.findByBlock(block).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public InfrastructureResponseDTO updateStatus(Long id, InfrastructureStatus status) {
        Infrastructure e = find(id);
        e.setStatus(status);
        return toResponse(repo.save(e));
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Infrastructure find(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Infrastructure not found"));
    }

    private void map(Infrastructure e, InfrastructureRequestDTO d) {
        e.setRoomOrLabName(d.getRoomOrLabName());
        e.setFloor(d.getFloor());
        e.setBlock(d.getBlock());
        e.setCapacity(d.getCapacity());
        e.setHasProjector(d.getHasProjector());
        e.setNoOfComputers(d.getNoOfComputers());
        e.setStatus(d.getStatus());
        e.setType(d.getType());
    }

    private InfrastructureResponseDTO toResponse(Infrastructure e) {
        InfrastructureResponseDTO d = new InfrastructureResponseDTO();
        d.setId(e.getId());
        d.setRoomOrLabName(e.getRoomOrLabName());
        d.setFloor(e.getFloor());
        d.setBlock(e.getBlock());
        d.setCapacity(e.getCapacity());
        d.setHasProjector(e.getHasProjector());
        d.setNoOfComputers(e.getNoOfComputers());
        d.setStatus(e.getStatus());
        d.setType(e.getType());
        d.setDepartmentName(e.getDepartment() != null ? e.getDepartment().getName() : null);
        return d;
    }
}   