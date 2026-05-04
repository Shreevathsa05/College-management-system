package com.ayush.College_Management_System.specifications;

import com.ayush.College_Management_System.model.Student;
import com.ayush.College_Management_System.model.enums.StudentStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class StudentSpecifications {

    public static Specification<Student> filterStudents(
            Long departmentId,
            Long courseId,
            StudentStatus status,
            Integer semester,
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

            if (passoutYear != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(criteriaBuilder.sum(root.get("admissionYear"), 4), passoutYear));
            }

            return predicate;
        };
    }
}
