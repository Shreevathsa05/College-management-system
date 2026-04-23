package com.ayush.College_Management_System.service.impl;

import com.ayush.College_Management_System.dto.classroom.*;
import com.ayush.College_Management_System.exception.ResourceNotFoundException;
import com.ayush.College_Management_System.model.*;
import com.ayush.College_Management_System.model.enums.ClassroomType;
import com.ayush.College_Management_System.repository.*;
import com.ayush.College_Management_System.service.ClassroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassroomServiceImpl implements ClassroomService {

    private final ClassroomRepository repo;
    private final DepartmentRepository deptRepo;

    // ── CRUD ─────────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public ClassroomResponseDTO create(ClassroomRequestDTO dto) {
        Department dept = deptRepo.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        Classroom e = new Classroom();
        map(e, dto);
        e.setDepartment(dept);
        return toResponse(repo.save(e));
    }

    @Override
    @Transactional(readOnly = true)
    public ClassroomResponseDTO getById(Long id) {
        return toResponse(find(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClassroomResponseDTO> getAll() {
        return repo.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ClassroomResponseDTO update(Long id, ClassroomRequestDTO dto) {
        Classroom e = find(id);
        Department dept = deptRepo.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        map(e, dto);
        e.setDepartment(dept);
        return toResponse(repo.save(e));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Classroom not found");
        repo.deleteById(id);
    }

    // ── Filters ───────────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<ClassroomResponseDTO> getByDepartment(Long departmentId) {
        return repo.findByDepartmentId(departmentId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClassroomResponseDTO> getByType(ClassroomType type) {
        return repo.findByClassroomType(type).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClassroomResponseDTO> getAvailable() {
        return repo.findByIsAvailable(true).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    // ── Availability toggle ───────────────────────────────────────────────────

    @Override
    @Transactional
    public ClassroomResponseDTO toggleAvailability(Long id, Boolean isAvailable) {
        Classroom e = find(id);
        e.setIsAvailable(isAvailable);
        return toResponse(repo.save(e));
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Classroom find(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found"));
    }

    private void map(Classroom e, ClassroomRequestDTO d) {
        e.setRoomNumber(d.getRoomNumber());
        e.setBuilding(d.getBuilding());
        e.setFloor(d.getFloor());
        e.setCapacity(d.getCapacity());
        e.setClassroomType(d.getClassroomType());
        e.setHasProjector(d.getHasProjector());
        e.setHasAC(d.getHasAC());
        if (d.getIsAvailable() != null) e.setIsAvailable(d.getIsAvailable());
    }

    private ClassroomResponseDTO toResponse(Classroom e) {
        ClassroomResponseDTO d = new ClassroomResponseDTO();
        d.setId(e.getId());
        d.setRoomNumber(e.getRoomNumber());
        d.setBuilding(e.getBuilding());
        d.setFloor(e.getFloor());
        d.setCapacity(e.getCapacity());
        d.setClassroomType(e.getClassroomType());
        d.setHasProjector(e.getHasProjector());
        d.setHasAC(e.getHasAC());
        d.setIsAvailable(e.getIsAvailable());
        d.setDepartmentName(e.getDepartment() != null ? e.getDepartment().getName() : null);
        return d;
    }
}