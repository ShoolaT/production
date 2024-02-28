package com.example.production.repository;

import com.example.production.model.Ingredient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long>{
    Page<Ingredient> findByProductId(Long productId, Pageable pageable);
    boolean existsByProduct_IdAndRawMaterial_Id(Long productId, Long materialId);
    @Query("SELECT i.id FROM Ingredient i WHERE i.product.id = :productId AND i.rawMaterial.id = :rawMaterialId")
    Long findIdByProductIdAndRawMaterialId(@Param("productId") Long productId, @Param("rawMaterialId") Long rawMaterialId);
    List<Ingredient> findByProduct_Id(Long productId);
    @Query("SELECT i FROM Ingredient i WHERE i.product.id = :productId AND i.rawMaterial.id = :rawMaterialId")
    Optional<Ingredient> findByProductIdAndRawMaterialId(@Param("productId") Long productId, @Param("rawMaterialId") Long rawMaterialId);

}
