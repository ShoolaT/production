package com.example.production.repository;

import com.example.production.model.ProductSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ProductSaleRepository extends JpaRepository<ProductSale, Long>{
    @Procedure(procedureName = "GetSalesByDate")
    List<ProductSale> getSalesByDate(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );
}
