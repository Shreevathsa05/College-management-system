package com.ayush.College_Management_System.model;

import com.ayush.College_Management_System.model.enums.InfrastructureStatus;
import com.ayush.College_Management_System.model.enums.InfrastructureType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "infrastructure",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_infra_block_room",
                columnNames = {"block", "roomOrLabName"}
        ),
        indexes = @Index(name = "idx_infra_dept", columnList = "dept_id")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"department"})
public class Infrastructure extends BaseEntity {

    // ── No @Id here — inherited from BaseEntity ──

    @Column(nullable = false)
    private String roomOrLabName;

    private Integer floor;

    @Column(nullable = false)
    private String block;

    private Integer capacity;
    private Boolean hasProjector;
    private Integer noOfComputers;

    /**
     * Replaces the old raw String status field.
     * Values: AVAILABLE | UNDER_MAINTENANCE | DECOMMISSIONED
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InfrastructureStatus status = InfrastructureStatus.AVAILABLE;

    /**
     * Classifies the room by purpose.
     * Values: CLASSROOM | LABORATORY | COMPUTER_LAB | SERVER_ROOM | OFFICE | WORKSHOP | COMMON_AREA
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InfrastructureType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id", nullable = false)
    private Department department;
}