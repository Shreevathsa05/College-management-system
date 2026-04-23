package com.ayush.College_Management_System.model;

import com.ayush.College_Management_System.model.enums.ExamType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(
        name = "exams",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_exam_subject_type_session",
                columnNames = {"sub_id", "examType", "session"}
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"subject", "classroom", "results"})
public class Exam extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExamType examType;

    /**
     * Academic session, e.g. "2025-26".
     * Enforced at DTO level with @Pattern.
     */
    @Column(nullable = false)
    private String session;

    @Column(nullable = false)
    private LocalDate examDate;

    // ── Time fields for conflict detection ──
    private LocalTime startTime;
    private LocalTime endTime;

    @Column(nullable = false)
    private Integer maxMarks;

    /**
     * Nullable — falls back to subject.passingMarks when null.
     */
    private Integer passingMarks;

    // ── FK to Classroom replaces the old raw String roomNumber ──
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_id", nullable = false)
    private Subject subject;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Results> results;
}