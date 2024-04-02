package com.example.production.repository;

import com.example.production.model.Employee;
import com.example.production.model.RawMaterialPurchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface RawMaterialPurchaseRepository extends JpaRepository<RawMaterialPurchase, Long>{
    boolean existsByRawMaterialId(Long rawMaterialId);

    @Query("SELECT i.id FROM RawMaterialPurchase i WHERE i.rawMaterial.id = :rawMaterialId")
    Long findIdByRawMaterialId(@Param("rawMaterialId") Long rawMaterialId);

    int countByEmployeeAndDateBetween(Employee employee, Date startDate, Date endDate);
    @Query("SELECT COUNT(mp) FROM RawMaterialPurchase mp WHERE mp.employee = :employee AND YEAR(mp.date) = :year AND MONTH(mp.date) = :month")
    int countByEmployeeAndYearAndMonth(@Param("employee") Employee employee, @Param("year") int year, @Param("month") int month);
}
