package com.example.production.repository;

import com.example.production.model.RawMaterialPurchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RawMaterialPurchaseRepository extends JpaRepository<RawMaterialPurchase, Long>{
    boolean existsByRawMaterialId(Long rawMaterialId);

    @Query("SELECT i.id FROM RawMaterialPurchase i WHERE i.rawMaterial.id = :rawMaterialId")
    Long findIdByRawMaterialId(@Param("rawMaterialId") Long rawMaterialId);
}
