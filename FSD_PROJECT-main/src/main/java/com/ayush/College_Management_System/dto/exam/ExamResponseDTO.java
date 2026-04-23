package com.ayush.College_Management_System.dto.exam;

import com.ayush.College_Management_System.model.enums.ExamType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamResponseDTO {

    private Long id;
    private ExamType examType;
    private String session;
    private LocalDate examDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer maxMarks;
    private Integer passingMarks;

    // classroom info (null when not yet assigned)
    private Long classroomId;
    private String classroomRoomNumber;
    private String classroomBuilding;

    // subject info
    private Long subjectId;
    private String subjectName;
    private String subjectCode;
    private Integer semesterNumber;

    // context (sourced from subject → course → department)
    private String courseName;
    private String departmentName;
}