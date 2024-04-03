package com.example.production.model;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Entity
@Table(name = "ingredients")
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "product", referencedColumnName = "id")
    private FinishedProduct product;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "raw_material", referencedColumnName = "id")
    private RawMaterial rawMaterial;
    private float quantity;
}

