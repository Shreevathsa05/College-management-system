package com.ayush.College_Management_System.dto.exam;

import com.ayush.College_Management_System.model.enums.ExamType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamRequestDTO {

    @NotNull(message = "Exam type is required")
    private ExamType examType;

    /**
     * Academic session in format "YYYY-YY", e.g. "2025-26".
     */
    @NotNull(message = "Session is required")
    @Pattern(regexp = "\\d{4}-\\d{2}", message = "Session must be in format YYYY-YY, e.g. 2025-26")
    private String session;

    @NotNull(message = "Exam date is required")
    private LocalDate examDate;

    private LocalTime startTime;
    private LocalTime endTime;

    @NotNull(message = "Max marks is required")
    @Min(value = 1, message = "Max marks must be at least 1")
    private Integer maxMarks;

    /**
     * Nullable — if null, the subject's passingMarks is used.
     */
    private Integer passingMarks;

    /**
     * Optional — classroom can be assigned later via a PATCH.
     */
    private Long classroomId;

    @NotNull(message = "Subject ID is required")
    private Long subjectId;
}