package com.ayush.College_Management_System.dto.classroom;

import com.ayush.College_Management_System.model.enums.ClassroomType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class ClassroomResponseDTO {
    private Long id;
    private String roomNumber;
    private String building;
    private Integer floor;
    private Integer capacity;
    private ClassroomType classroomType;
    private Boolean hasProjector;
    private Boolean hasAC;
    private Boolean isAvailable;
    private String departmentName;
}