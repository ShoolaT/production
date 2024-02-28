package com.example.production.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Entity
public class RawMaterialPurchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "raw_material_id", referencedColumnName = "id")
    private RawMaterial rawMaterial;
//    @Convert(converter = DoubleToStringConverter.class)
    private float quantity;
//    @Convert(converter = DoubleToStringConverter.class)
    private float amount;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private Employee employee;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
}

