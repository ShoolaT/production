package com.example.production.model;

import jakarta.persistence.*;
import lombok.*;



@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "position", referencedColumnName = "id")
    private Position position;
    private float salary;
    private String address;
    private String phoneNumber;
}

