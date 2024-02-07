package com.example.production.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RawMaterialDto {
    private Long id;
    private String name;
    private UnitOfMeasurementDto unitsOfMeasurement;
    private double quantity;
    private double amount;
}

