package com.example.production.service;

import com.example.production.dto.ProductProductionDto;
import com.example.production.model.Bank;
import com.example.production.model.Employee;
import com.example.production.model.FinishedProduct;
import com.example.production.model.ProductProduction;
import com.example.production.repository.ProductProductionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductProductionService {
    private final ProductProductionRepository productionRepository;
    private final FinishedProductService productService;
    private final EmployeeService employeeService;

    @Transactional
    public List<ProductProduction> getProductProduction(Date startDate, Date endDate) {
        if (startDate == null && endDate == null) {
            return productionRepository.getProductionsByDate(null, null);
        } else {
            return productionRepository.getProductionsByDate(startDate, endDate);
        }
    }

    @Transactional
    public ProductProductionDto saveProductProduction(ProductProductionDto productionDto) {
        ProductProduction production = convertToEntity(productionDto);
        production = productionRepository.save(production);
        productionRepository.afterProductionProcedure(production.getProduct().getId(), production.getQuantity());

        return convertToDto(production);
    }
    public float calculateTotalQuantity(List<ProductProduction> productions) {
        float totalQuantity = 0;
        for (ProductProduction production : productions) {
            totalQuantity += production.getQuantity();
        }
        return totalQuantity;
    }


    public ProductProductionDto convertToDto(ProductProduction production) {
        var product = productService.getFinishedProductById(production.getProduct().getId());
        var employee = employeeService.getEmployeeById(production.getEmployee().getId());
        return ProductProductionDto.builder()
                .id(production.getId())
                .product(product)
                .quantity(production.getQuantity())
                .date(production.getDate())
                .employee(employee)
                .build();
    }

    private ProductProduction convertToEntity(ProductProductionDto productionDto) {
        FinishedProduct product = productService.getFinishedProduct(productionDto.getProduct().getId()).get();
        Employee employee = employeeService.getEmployee(productionDto.getEmployee().getId()).get();
        return ProductProduction.builder()
                .id(productionDto.getId())
                .product(product)
                .quantity(productionDto.getQuantity())
                .employee(employee)
                .date(productionDto.getDate())
                .build();
    }

}
