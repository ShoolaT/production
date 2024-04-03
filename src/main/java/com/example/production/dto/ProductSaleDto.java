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
public class ProductSaleDto {
    private Long id;
    private FinishedProductDto product;
    private float quantity;
    private float amount;
    private EmployeeDto employee;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
}

