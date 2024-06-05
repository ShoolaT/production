package com.example.production.repository;

import com.example.production.model.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, Long>{
    @Query(value = "EXEC dbo.GetSalariesByYearAndMonth ?, ?", nativeQuery = true)
    List<Salary> findByYearAndMonth(@Param("year") int yearStart,
                                    @Param("month") int monthStart);
    @Procedure(procedureName = "UpdateSalary")
    void updateSalary(@Param("SalaryId") Long salaryId, @Param("General") float general);
    @Procedure(procedureName = "IssueSalaries")
    boolean issueSalaries(@Param("year") int year, @Param("month") int month);
    @Query(value = "SELECT dbo.CalculateTotalSalary(:year, :month)", nativeQuery = true)
    Float calculateTotalSalary(@Param("year") int year, @Param("month") int month);
    @Procedure(procedureName = "GetSalariesByDate")
    List<Salary> getSalariesByDate(@Param("year_start") int yearStart,
                                    @Param("month_start") int monthStart,
                                    @Param("year_end") int yearEnd,
                                    @Param("month_end") int monthEnd);
}
