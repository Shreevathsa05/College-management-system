package com.ayush.College_Management_System.dto.infrastructure;

import com.ayush.College_Management_System.model.enums.InfrastructureStatus;
import com.ayush.College_Management_System.model.enums.InfrastructureType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InfrastructureResponseDTO {

    private Long id;
    private String roomOrLabName;
    private Integer floor;
    private String block;
    private Integer capacity;
    private Boolean hasProjector;
    private Integer noOfComputers;
    private InfrastructureStatus status;
    private InfrastructureType type;
    private String departmentName;
}