package com.example.production.repository;

import com.example.production.model.Employee;
import com.example.production.model.ProductProduction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ProductProductionRepository extends JpaRepository<ProductProduction, Long> {
     @Procedure(procedureName = "AfterProductionProcedure")
    void afterProductionProcedure(@Param("productId")Long product, @Param("Quantity")float quantity);
    @Procedure(procedureName = "GetProductionsByDate")
    List<ProductProduction> getProductionsByDate(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );
}
