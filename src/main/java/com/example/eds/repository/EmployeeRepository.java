package com.example.eds.repository;

import com.example.eds.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("""
            SELECT e FROM Employee e 
            WHERE (:departmentId IS NULL OR e.department.id = :departmentId)
            AND (:positionId IS NULL OR e.position.id = :positionId)
            AND (:keyword IS NULL OR e.name LIKE %:keyword%)
    """)
    Page<Employee> search(
            @Param("departmentId") Long departmentId,
            @Param("positionId") Long positionId,
            @Param("keyword") String keyword,
            Pageable pageable
    );
}
