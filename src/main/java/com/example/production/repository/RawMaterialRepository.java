package com.example.production.repository;

import com.example.production.model.Ingredient;
import com.example.production.model.RawMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RawMaterialRepository extends JpaRepository<RawMaterial, Long>{
    @Procedure(procedureName = "SP_checkRawMaterial")
    boolean checkRawMaterial(@Param("productId") Long productId, @Param("Quantity") float quantity);
}
