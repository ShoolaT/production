package com.example.production.repository;

import com.example.production.model.UnitsOfMeasurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitOfMeasurementRepository extends JpaRepository<UnitsOfMeasurement, Long>{
}
