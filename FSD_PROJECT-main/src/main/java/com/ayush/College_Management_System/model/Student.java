package com.ayush.College_Management_System.model;

import com.ayush.College_Management_System.model.enums.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "students",
        indexes = @Index(name = "idx_roll", columnList = "rollNo"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"attendanceRecords", "feeRecords", "results", "department", "course"})
public class Student extends BaseEntity {

    @Column(nullable = false)
    private String firstName;

    private String lastName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate dob;

    @Column(unique = true, nullable = false)
    private String rollNo;

    @Column(unique = true)
    private String email;

    private String phone;
    private Integer currentSemester;
    private Integer admissionYear;
    @Enumerated(EnumType.STRING)
    private StudentStatus status;

    private String address;

    // ── New fields ──
    @Column(unique = true)
    private String enrollmentNumber;

    @Enumerated(EnumType.STRING)
    private AdmissionType admissionType;

    @Enumerated(EnumType.STRING)
    private BloodGroup bloodGroup;

    private String profileImageUrl;

    @Column(nullable = false)
    @ColumnDefault("true")
    private Boolean isActive = true;

    // ── Relationships ──
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id", nullable = false)
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attendance> attendanceRecords;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Fees> feeRecords;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Results> results;

}