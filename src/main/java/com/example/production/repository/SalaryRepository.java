package com.example.production.repository;

import com.example.production.model.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, Long>{
    List<Salary> findByYearAndMonth(int year, int month);
}
