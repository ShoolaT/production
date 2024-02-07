package com.example.production.repository;

import com.example.production.model.FinishedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinishedProductRepository extends JpaRepository<FinishedProduct, Long>{
}
