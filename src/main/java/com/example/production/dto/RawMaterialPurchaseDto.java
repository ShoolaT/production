package com.example.production.dto;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
public class RawMaterialPurchaseDto {
    private Long id;
    private RawMaterialDto rawMaterial;
    private float quantity;
    private float amount;
    private EmployeeDto employee;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
}

