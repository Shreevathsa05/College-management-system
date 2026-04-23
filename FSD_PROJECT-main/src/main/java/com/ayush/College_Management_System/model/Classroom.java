package com.ayush.College_Management_System.model;

import com.ayush.College_Management_System.model.enums.ClassroomType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "classes",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_classroom_room_building",
                columnNames = {"roomNumber", "building"}
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"department"})
public class Classroom extends BaseEntity {

    @Column(nullable = false)
    private String roomNumber;

    @Column(nullable = false)
    private String building;

    private Integer floor;

    @Column(nullable = false)
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClassroomType classroomType;

    private Boolean hasProjector;
    private Boolean hasAC;

    @Column(nullable = false)
    private Boolean isAvailable = true;

    // ── nullable=false added — a classroom must belong to a department ──
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id", nullable = false)
    private Department department;
}