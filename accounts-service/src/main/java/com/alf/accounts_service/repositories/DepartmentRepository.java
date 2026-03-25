package com.alf.accounts_service.repositories;

import com.alf.accounts_service.models.core.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    // Find all departments belonging to a specific company
    // Find all departments belonging to a specific company
    List<Department> findByAccountId(Integer accountId);

    // Find a specific department by its ID and company ID (for security/scoping)
    Optional<Department> findByIdAndAccountId(Integer id, Integer accountId);

    // Find a department by its unique serial ID within a company
    Optional<Department> findBySerialIdAndAccountId(String serialId, Integer accountId);

    @Query("SELECT d FROM Department d WHERE d.serialId = :id AND d.account.id = :accountId")
    Optional<Department> findBySerialIdAndAccountId_IgnoreSoftDelete(@Param("id") String id ,@Param("accountId") Integer accountId);

    // Find departments that have a specific parent serial ID within a company (find children)
    List<Department> findByParentSerialIdAndAccountId(String parentSerialId, Integer accountId);

    // Check if a serial ID already exists within a company
    boolean existsBySerialIdAndAccountId(String serialId, Integer accountId);

    // Find root departments (those with null parent_serial_id) within a company
    List<Department> findByAccountIdAndParentSerialIdIsNull(Integer accountId);

    // Find departments by name within a company (example custom query)
    List<Department> findByNameContainingIgnoreCaseAndAccountId(String name, Integer accountId);


}
