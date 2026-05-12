package com.ems.employee;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Page<Employee> findByIsDeletedFalse(Pageable pageable);

    Page<Employee> findByDepartmentIdAndIsDeletedFalse(Long departmentId, Pageable pageable);

    Optional<Employee> findByIdAndIsDeletedFalse(Long id);

    Optional<Employee> findByUserIdAndIsDeletedFalse(Long userId);

    Optional<Employee> findByEmailAndIsDeletedFalse(String email);

    @Query("SELECT COUNT(e) FROM Employee e WHERE e.isDeleted = false")
    long countActive();

    @Query("SELECT COUNT(e) FROM Employee e WHERE e.isDeleted = false AND e.status = :status")
    long countByStatus(@Param("status") String status);

    @Query("SELECT COUNT(e) FROM Employee e WHERE e.isDeleted = false AND e.departmentId = :deptId")
    long countByDepartmentId(@Param("deptId") Long departmentId);

    @Query("SELECT e FROM Employee e WHERE e.isDeleted = false ORDER BY e.hireDate DESC")
    List<Employee> findRecentHires(Pageable pageable);

    @Query("SELECT e FROM Employee e WHERE e.isDeleted = false AND " +
           "(LOWER(e.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(e.email) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Employee> searchEmployees(@Param("search") String search, Pageable pageable);
}
