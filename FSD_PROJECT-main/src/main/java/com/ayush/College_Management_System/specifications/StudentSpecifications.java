package com.ayush.College_Management_System.specifications;

import com.ayush.College_Management_System.model.Student;
import com.ayush.College_Management_System.model.enums.AdmissionType;
import com.ayush.College_Management_System.model.enums.StudentStatus;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class StudentSpecifications {

    public static Specification<Student> filterStudents(
            Long departmentId,
            Long courseId,
            StudentStatus status,
            Integer semester,
            AdmissionType admissionType,
            Integer passoutYear
    ) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (departmentId != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("department").get("id"), departmentId));
            }

            if (courseId != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("course").get("id"), courseId));
            }

            if (status != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("status"), status));
            }

            if (semester != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("currentSemester"), semester));
            }

            if (admissionType != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("admissionType"), admissionType));
            }

            if (passoutYear != null) {
                Expression<Integer> calculatedPassoutYear = criteriaBuilder.<Integer>selectCase()
                        .when(
                                criteriaBuilder.equal(root.get("admissionType"), AdmissionType.LATERAL),
                                criteriaBuilder.sum(root.get("admissionYear"), 3)
                        )
                        .otherwise(criteriaBuilder.sum(root.get("admissionYear"), 4));

                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(calculatedPassoutYear, passoutYear));
            }

            return predicate;
        };
    }
}
