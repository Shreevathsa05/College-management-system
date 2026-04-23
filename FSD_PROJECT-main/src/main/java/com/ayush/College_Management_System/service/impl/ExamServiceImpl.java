package com.ayush.College_Management_System.service.impl;

import com.ayush.College_Management_System.dto.exam.*;
import com.ayush.College_Management_System.exception.ResourceNotFoundException;
import com.ayush.College_Management_System.model.*;
import com.ayush.College_Management_System.model.enums.ExamType;
import com.ayush.College_Management_System.repository.*;
import com.ayush.College_Management_System.security.SecurityUserAccessor;
import com.ayush.College_Management_System.security.user.AppUserDetails;
import com.ayush.College_Management_System.security.user.Role;
import com.ayush.College_Management_System.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {

    private final ExamRepository repo;
    private final SubjectRepository subjectRepo;
    private final ClassroomRepository classroomRepo;
    private final SecurityUserAccessor securityUserAccessor;

    // ── CRUD ─────────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public ExamResponseDTO create(ExamRequestDTO dto) {
        Subject subject = subjectRepo.findById(dto.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));
        assertFacultyOwnsSubjectForExam(subject);

        Exam e = new Exam();
        map(e, dto, subject);
        return toResponse(repo.save(e));
    }

    @Override
    @Transactional(readOnly = true)
    public ExamResponseDTO getById(Long id) {
        return toResponse(find(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamResponseDTO> getAll() {
        return repo.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ExamResponseDTO update(Long id, ExamRequestDTO dto) {
        Exam e = find(id);
        Subject subject = subjectRepo.findById(dto.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));
        assertFacultyOwnsSubjectForExam(subject);
        map(e, dto, subject);
        return toResponse(repo.save(e));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Exam e = find(id);
        // Guard: do not silently wipe results — cascade=ALL would delete them all
        if (e.getResults() != null && !e.getResults().isEmpty()) {
            throw new IllegalStateException(
                    "Cannot delete exam with existing results. Remove results first or archive the exam.");
        }
        repo.delete(e);
    }

    // ── Filters ───────────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<ExamResponseDTO> getBySubject(Long subjectId) {
        return repo.findBySubjectId(subjectId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamResponseDTO> getByExamType(ExamType examType) {
        return repo.findByExamType(examType).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamResponseDTO> getBySession(String session) {
        return repo.findBySession(session).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamResponseDTO> getByDateRange(LocalDate from, LocalDate to) {
        return repo.findByExamDateBetween(from, to).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamResponseDTO> getUpcoming() {
        return repo.findByExamDateAfter(LocalDate.now()).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamResponseDTO> getByClassroom(Long classroomId) {
        return repo.findByClassroomId(classroomId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    // ── Security ─────────────────────────────────────────────────────────────

    private void assertFacultyOwnsSubjectForExam(Subject subject) {
        AppUserDetails user = securityUserAccessor.getCurrentUser();
        if (user.getRole() == Role.ADMIN) return;
        if (user.getRole() == Role.FACULTY) {
            if (user.getLinkedId() == null) {
                throw new AccessDeniedException(
                        "FACULTY users must have linkedId set to their faculty record id to manage exams");
            }
            Faculty assigned = subject.getFaculty();
            if (assigned == null || !user.getLinkedId().equals(assigned.getId())) {
                throw new AccessDeniedException("You can only manage exams for your own subjects");
            }
            return;
        }
        throw new AccessDeniedException("You do not have permission to manage exams");
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Exam find(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found"));
    }

    private void map(Exam e, ExamRequestDTO d, Subject subject) {
        e.setExamType(d.getExamType());
        e.setSession(d.getSession());
        e.setExamDate(d.getExamDate());
        e.setStartTime(d.getStartTime());
        e.setEndTime(d.getEndTime());
        e.setMaxMarks(d.getMaxMarks());
        e.setPassingMarks(d.getPassingMarks());
        e.setSubject(subject);

        // Resolve optional classroom FK
        if (d.getClassroomId() != null) {
            Classroom classroom = classroomRepo.findById(d.getClassroomId())
                    .orElseThrow(() -> new ResourceNotFoundException("Classroom not found"));
            e.setClassroom(classroom);
        } else {
            e.setClassroom(null);
        }
    }

    private ExamResponseDTO toResponse(Exam e) {
        ExamResponseDTO d = new ExamResponseDTO();
        d.setId(e.getId());
        d.setExamType(e.getExamType());
        d.setSession(e.getSession());
        d.setExamDate(e.getExamDate());
        d.setStartTime(e.getStartTime());
        d.setEndTime(e.getEndTime());
        d.setMaxMarks(e.getMaxMarks());
        d.setPassingMarks(e.getPassingMarks());

        // Classroom info
        if (e.getClassroom() != null) {
            d.setClassroomId(e.getClassroom().getId());
            d.setClassroomRoomNumber(e.getClassroom().getRoomNumber());
            d.setClassroomBuilding(e.getClassroom().getBuilding());
        }

        // Subject info
        Subject s = e.getSubject();
        if (s != null) {
            d.setSubjectId(s.getId());
            d.setSubjectName(s.getSubName());
            d.setSubjectCode(s.getSubCode());
            d.setSemesterNumber(s.getSemesterNumber());
            // Course & department context
            if (s.getCourse() != null) {
                d.setCourseName(s.getCourse().getCourseTitle());
                if (s.getCourse().getDepartment() != null) {
                    d.setDepartmentName(s.getCourse().getDepartment().getName());
                }
            }
        }
        return d;
    }
}