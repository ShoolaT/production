package com.example.production.repository;

import com.example.production.model.Bank;
import com.example.production.model.ProductProduction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {

    @Procedure(procedureName = "CreateBank")
    Long createBank(
            @Param("sum") float sum,
            @Param("months") int months,
            @Param("percent") float percent,
            @Param("fine") float fine,
            @Param("receiptDate") Date receiptDate,
            @Param("isPaid") boolean paid);

    @Procedure(procedureName = "GetBanksByDate")
    List<Bank> getBanksByDate(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );
}
