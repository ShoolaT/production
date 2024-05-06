package com.example.production.repository;

import com.example.production.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
    @Procedure(procedureName = "CreateEmployeeByRole")
    void createRole(
            @Param("employee_id") Long employeeId,
            @Param("role_id") Long roleId
    );
    @Procedure(procedureName = "GetIdByRole")
    Long getIdByRole(@Param("role") String authority);
    @Procedure(procedureName = "DeleteRoleByEmployeeId")
    void deleteRoleByEmployeeId(@Param("employee_id") Long employeeId);

}
