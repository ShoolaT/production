package com.example.production.repository;

import com.example.production.model.Employee;
import com.example.production.model.ProductSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface ProductSaleRepository extends JpaRepository<ProductSale, Long>{
    int countByEmployeeAndDateBetween(Employee employee, Date startDate, Date endDate);
    @Query("SELECT COUNT(ps) FROM ProductSale ps WHERE ps.employee = :employee AND YEAR(ps.date) = :year AND MONTH(ps.date) = :month")
    int countByEmployeeYearAndMonth(@Param("employee") Employee employee, @Param("year") int year, @Param("month") int month);

}
