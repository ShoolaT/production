package com.example.production.repository;

import com.example.production.model.Employee;
import com.example.production.model.RawMaterialPurchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RawMaterialPurchaseRepository extends JpaRepository<RawMaterialPurchase, Long>{
     @Procedure(procedureName = "GetMaterialPurchasesByDate")
    List<RawMaterialPurchase> getMaterialPurchasesByDate(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );
    }
