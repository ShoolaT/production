package com.example.production.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
    private Long id;
    @NotBlank
    private String fullName;
    private PositionDto position;
    @NotNull
    private float salary;
    @NotBlank
    private String address;
    private String phoneNumber;
}

