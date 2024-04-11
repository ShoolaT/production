package com.example.production.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@Getter
@Setter
@Builder
@AllArgsConstructor(access =   AccessLevel.PRIVATE)
@NoArgsConstructor
@Entity
@Table(name = "salary")
public class Salary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int year;
    private int month;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private Employee employee;
    private int numberOfPurchase;
    private int numberOfProduction;
    private int numberOfSale;
    private int common;// сумма участий
    private float salary; // оклад сотрудника
    private float bonus;
    private float general; // итоговая зп
    private boolean issued;
}

