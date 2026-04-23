package com.ayush.College_Management_System.dto.infrastructure;

import com.ayush.College_Management_System.model.enums.InfrastructureStatus;
import com.ayush.College_Management_System.model.enums.InfrastructureType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InfrastructureRequestDTO {

    @NotBlank(message = "Room or lab name is required")
    private String roomOrLabName;

    private Integer floor;

    @NotBlank(message = "Block is required")
    private String block;

    @Min(value = 1, message = "Capacity must be at least 1")
    private Integer capacity;

    private Boolean hasProjector;
    private Integer noOfComputers;

    @NotNull(message = "Status is required")
    private InfrastructureStatus status;

    @NotNull(message = "Type is required")
    private InfrastructureType type;

    @NotNull(message = "Department ID is required")
    private Long departmentId;
}