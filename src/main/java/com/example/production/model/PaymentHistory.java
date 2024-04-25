package com.example.production.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Entity
@Table(name="payment_history")
public class PaymentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date paymentDate;
    private Float baseAmount;// основная сумма = сумма кредита/кол-во месяцев
    private Float percentAmount;// процентная сумма
    private Float allAmount; // общая сумма = основная сумма + процентная сумма
    private int overdue; // кол-во просроченных дней
    private Float fineAmount; // сумма пеней
    private Float totalAmount; // Итого = общая сумма + сумма пеней
    private Float residue;// остаток = сумма кредита - основная сумма
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "bank_id", referencedColumnName = "id")
    private Bank bank;

}
