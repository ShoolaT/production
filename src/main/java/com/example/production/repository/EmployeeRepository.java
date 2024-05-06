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
}
