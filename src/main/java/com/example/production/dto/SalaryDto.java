package com.example.production.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SalaryDto {
    private Long id;
    private int year;
    private int month;
    private String employeeName;
    private int numberOfPurchase;
    private int numberOfProduction;
    private int numberOfSale;
    private int common;// сумма участий
    private float salary; // оклад сотрудника
    private float bonus;
    private float general; // итоговая зп
    private boolean issued;
}

