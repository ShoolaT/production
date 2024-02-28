package com.example.production.model;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Entity
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private FinishedProduct product;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "raw_material_id", referencedColumnName = "id")
    private RawMaterial rawMaterial;
    private float quantity;
}

