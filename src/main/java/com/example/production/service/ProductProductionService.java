package com.example.production.service;

import com.example.production.dto.ProductProductionDto;
import com.example.production.model.Employee;
import com.example.production.model.FinishedProduct;
import com.example.production.model.ProductProduction;
import com.example.production.repository.ProductProductionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductProductionService {
    private final ProductProductionRepository productionRepository;
    private final FinishedProductService productService;
    private final EmployeeService employeeService;

    public Page<ProductProductionDto> getProductProduction(int page, int size, String sort) {
        var list = productionRepository.findAll(PageRequest.of(page, size, Sort.by(sort)));
        return toPage(list.getContent(), PageRequest.of(list.getNumber(), list.getSize(), list.getSort()));
    }

    private Page<ProductProductionDto> toPage(List<ProductProduction> productProductions, Pageable pageable) {
        var list = productProductions.stream()
                .map(this::convertToDto)
                .toList();
        if (pageable.getOffset() >= list.size()) {
            return Page.empty();
        }
        int startIndex = (int) pageable.getOffset();
        int endIndex = (int) ((pageable.getOffset() + pageable.getPageSize()) > list.size() ?
                list.size() :
                pageable.getOffset() + pageable.getPageSize());
        List<ProductProductionDto> subList = list.subList(startIndex, endIndex);
        return new PageImpl<>(subList, pageable, list.size());
    }

    @Transactional
    public ProductProductionDto saveProductProduction(ProductProductionDto productionDto) {
        ProductProduction production = convertToEntity(productionDto);
        production = productionRepository.save(production);
        productionRepository.afterProductionProcedure(production.getProduct().getId(),production.getQuantity());

        return convertToDto(production);
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
