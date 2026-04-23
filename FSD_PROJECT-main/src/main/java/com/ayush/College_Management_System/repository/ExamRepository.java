package com.ayush.College_Management_System.repository;

import com.ayush.College_Management_System.model.Exam;
import com.ayush.College_Management_System.model.enums.ExamType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

    long countByExamDateAfter(LocalDate date);

    List<Exam> findBySubjectId(Long subjectId);

    List<Exam> findByExamType(ExamType examType);

    List<Exam> findBySession(String session);

    List<Exam> findByExamDateBetween(LocalDate from, LocalDate to);

    List<Exam> findByExamDateAfter(LocalDate date);

    List<Exam> findByClassroomId(Long classroomId);

    List<Exam> findBySubjectIdAndExamType(Long subjectId, ExamType examType);
}