package com.example.production.repository;

import com.example.production.model.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {
    @Query(value = "EXEC dbo.getPaymentHistoryForPay ?, ?", nativeQuery = true)
    PaymentHistory getPaymentHistory(
            @Param("bankId") Long bankId,
            @Param("date") Date date);

    @Procedure(procedureName = "GetPaymentHistoriesByBankId")
    List<PaymentHistory> findAllByBankId(@Param("bankId") Long bankId);

    @Procedure(procedureName = "CreatePaymentHistory")
    Long savePaymentHistory(
            @Param("paymentDate") Date paymentDate,
            @Param("baseAmount") Float baseAmount,
            @Param("percentAmount") Float percentAmount,
            @Param("allAmount") Float allAmount,
            @Param("overdue") Integer overdue,
            @Param("fineAmount") Float fineAmount,
            @Param("totalAmount") Float totalAmount,
            @Param("residue") Float residue,
            @Param("bankId") Long bankId
    );
    @Procedure(procedureName = "GetPaymentTotalAmount")
    float getPaymentTotalAmount(@Param("bankId") Long bankId);
}
