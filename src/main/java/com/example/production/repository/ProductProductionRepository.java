package com.example.production.repository;

import com.example.production.model.ProductProduction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductProductionRepository extends JpaRepository<ProductProduction, Long>{
}
