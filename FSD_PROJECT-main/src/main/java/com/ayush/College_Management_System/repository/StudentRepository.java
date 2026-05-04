package com.ayush.College_Management_System.repository;

import com.ayush.College_Management_System.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByRollNo(String rollNo);

    Optional<Student> findByEnrollmentNumber(String enrollmentNumber);

    boolean existsByRollNo(String rollNo);

    boolean existsByEnrollmentNumber(String enrollmentNumber);

     @Query("""
    SELECT s FROM Student s
    WHERE LOWER(s.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(s.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(s.rollNo) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(s.enrollmentNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))
       """)
    List<Student> searchStudents(@Param("keyword") String keyword);
}