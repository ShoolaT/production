package com.example.production.repository;

import com.example.production.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>{
    @Query("SELECT u FROM Employee u WHERE u.email = :email")
    Employee getUserByUsername(@Param("email") String email);
    @Query("SELECT u.fullName FROM Employee u WHERE u.email = :email")
    String getFullNameByEmail(@Param("email") String email);
    @Query("SELECT u.fullName, u.position.name FROM Employee u WHERE u.email = :email")
    String getEmployeeDetailsByEmail(@Param("email") String email);

}
