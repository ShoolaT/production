package com.example.production.repository;

import com.example.production.model.Ingredient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long>{
    @Procedure(procedureName = "GetIngredients")
    List<Ingredient> getIngredients();
    @Procedure(procedureName = "GetIngredientsByProductId")
    List<Ingredient> findByProductId(Long productId);
    boolean existsByProduct_IdAndRawMaterial_Id(Long productId, Long materialId);
    @Query("SELECT i.id FROM Ingredient i WHERE i.product.id = :productId AND i.rawMaterial.id = :rawMaterialId")
    Long findIdByProductIdAndRawMaterialId(@Param("productId") Long productId, @Param("rawMaterialId") Long rawMaterialId);
    List<Ingredient> findByProduct_Id(Long productId);
    @Procedure(procedureName = "CreateIngredient")
    Long createIngredient(@Param("ProductId") Long productId, @Param("RawMaterialId") Long rawMaterialId, @Param("Quantity") float quantity);
    @Procedure(procedureName = "UpdateIngredient")
    void updateIngredient(@Param("IngredientId") Long ingredientId, @Param("ProductId") Long productId, @Param("RawMaterialId") Long rawMaterialId, @Param("Quantity") float quantity);
    @Procedure(procedureName = "DeleteIngredient")
    void deleteIngredient(@Param("IngredientId") Long ingredientId);
}
