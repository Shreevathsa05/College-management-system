package com.ayush.College_Management_System.repository;

import com.ayush.College_Management_System.model.Infrastructure;
import com.ayush.College_Management_System.model.enums.InfrastructureStatus;
import com.ayush.College_Management_System.model.enums.InfrastructureType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InfrastructureRepository extends JpaRepository<Infrastructure, Long> {

    List<Infrastructure> findByDepartmentId(Long departmentId);

    List<Infrastructure> findByStatus(InfrastructureStatus status);

    List<Infrastructure> findByType(InfrastructureType type);

    List<Infrastructure> findByBlock(String block);

    List<Infrastructure> findByDepartmentIdAndStatus(Long departmentId, InfrastructureStatus status);

    List<Infrastructure> findByDepartmentIdAndType(Long departmentId, InfrastructureType type);
}