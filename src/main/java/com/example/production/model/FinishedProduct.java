package com.example.production.model;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Entity
@Table(name = "finished_products")
public class FinishedProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "unit_of_measurement", referencedColumnName = "id")
    private UnitsOfMeasurement unitsOfMeasurement;
    private float quantity;
    private float amount;
}

