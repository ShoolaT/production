package com.example.production.repository;

import com.example.production.model.Employee;
import com.example.production.model.ProductProduction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface ProductProductionRepository extends JpaRepository<ProductProduction, Long> {
    int countByEmployeeAndDateBetween(Employee employee, Date startDate, Date endDate);

    @Query("SELECT COUNT(pp) FROM ProductProduction pp WHERE pp.employee = :employee AND YEAR(pp.date) = :year AND MONTH(pp.date) = :month")
    int countByEmployeeAndYearAndMonth(@Param("employee") Employee employee, @Param("year") int year, @Param("month") int month);
}
