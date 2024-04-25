package com.example.production.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentHistoryDto {
    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date paymentDate;
    private Float baseAmount;// основная сумма = сумма кредита/кол-во месяцев
    private Float percentAmount;// процентная сумма
    private Float allAmount; // общая сумма = основная сумма + процентная сумма
    private int overdue; // кол-во просроченных дней
    private Float fineAmount; // сумма пеней
    private Float totalAmount; // Итого = общая сумма + сумма пеней
    private Float residue;// остаток = сумма кредита - основная сумма
    private BankDto bank;

}
