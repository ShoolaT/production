package com.example.production.repository;

import com.example.production.model.FinishedProduct;
import com.example.production.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinishedProductRepository extends JpaRepository<FinishedProduct, Long>{
    @Procedure(procedureName = "GetFinishedProducts")
    List<FinishedProduct> getFinishedProducts();
    @Procedure(procedureName = "CreateFinishedProduct")
    Long createFinishedProduct(
            @Param("ProductName") String productName,
            @Param("UnitOfMeasurementId") Long unitOfMeasurementId
    );
    @Procedure(procedureName = "UpdateFinishedProduct")
    void updateFinishedProduct(
            @Param("ProductId") Long productId,
            @Param("ProductName") String productName,
            @Param("UnitOfMeasurementId") Long unitOfMeasurementId
    );


}
