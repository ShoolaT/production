package com.example.production.repository;

import com.example.production.model.ProductSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSaleRepository extends JpaRepository<ProductSale, Long>{
}
