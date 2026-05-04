package com.ayush.College_Management_System.specification;

import com.ayush.College_Management_System.model.Student;
import com.ayush.College_Management_System.model.enums.StudentStatus;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.*;

public class StudentSpecification {

    public static Specification<Student> filterStudents(
            Long departmentId,
            Long courseId,
            StudentStatus status,
            Integer semester,
            Integer passoutYear
    ) {
        return (Root<Student> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {

            Predicate predicate = cb.conjunction();

            if (departmentId != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("department").get("id"), departmentId));
            }

            if (courseId != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("course").get("id"), courseId));
            }

            if (status != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("status"), status));
            }

            if (semester != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("currentSemester"), semester));
            }

            if (passoutYear != null) {
                Expression<Integer> admissionYear = root.get("admissionYear");
                Expression<Integer> calculatedPassoutYear = cb.sum(admissionYear, 4);

                predicate = cb.and(predicate,
                        cb.equal(calculatedPassoutYear, passoutYear));
            }

            return predicate;
        };
    }
}