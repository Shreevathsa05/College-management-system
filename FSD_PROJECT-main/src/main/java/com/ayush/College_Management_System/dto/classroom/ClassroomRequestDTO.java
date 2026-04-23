package com.ayush.College_Management_System.dto.classroom;

import com.ayush.College_Management_System.model.enums.ClassroomType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassroomRequestDTO {

    @NotBlank(message = "Room number is required")
    private String roomNumber;

    @NotBlank(message = "Building is required")
    private String building;

    private Integer floor;

    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    private Integer capacity;

    @NotNull(message = "Classroom type is required")
    private ClassroomType classroomType;

    private Boolean hasProjector;
    private Boolean hasAC;
    private Boolean isAvailable;

    @NotNull(message = "Department ID is required")
    private Long departmentId;
}