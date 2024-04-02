package com.example.production.service;

import com.example.production.dto.ProductSaleDto;
import com.example.production.model.Employee;
import com.example.production.model.FinishedProduct;
import com.example.production.model.ProductSale;
import com.example.production.repository.ProductSaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductSaleService {
    private final ProductSaleRepository productSaleRepository;
    private final FinishedProductService productService;
    private final EmployeeService employeeService;

    public Page<ProductSaleDto> getProductSales(int page, int size, String sort) {
        var list = productSaleRepository.findAll(PageRequest.of(page, size, Sort.by(sort)));
        return toPage(list.getContent(), PageRequest.of(list.getNumber(), list.getSize(), list.getSort()));
    }

    private Page<ProductSaleDto> toPage(List<ProductSale> productSales, Pageable pageable) {
        var list = productSales.stream()
                .map(this::convertToDto)
                .toList();
        if (pageable.getOffset() >= list.size()) {
            return Page.empty();
        }
        int startIndex = (int) pageable.getOffset();
        int endIndex = (int) ((pageable.getOffset() + pageable.getPageSize()) > list.size() ?
                list.size() :
                pageable.getOffset() + pageable.getPageSize());
        List<ProductSaleDto> subList = list.subList(startIndex, endIndex);
        return new PageImpl<>(subList, pageable, list.size());
    }

    public List<ProductSaleDto> getAllProductSales() {
        List<ProductSale> productSales = productSaleRepository.findAll();
        return productSales.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ProductSaleDto getProductSaleById(Long id) {
        var productSale = productSaleRepository.findById(id).get();
        return convertToDto(productSale);
    }

    public Optional<ProductSale> getProductSale(Long id) {
        return Optional.of(productSaleRepository.findById(id).get());
    }

    public ProductSaleDto saveProductSale(ProductSaleDto productSaleDto) {
        ProductSale productSale = convertToEntity(productSaleDto);
        productSale = productSaleRepository.save(productSale);
        return convertToDto(productSale);
    }

    public ProductSaleDto updateProductSale(ProductSaleDto productSaleDto) {
        boolean existingProductSale = productSaleRepository.existsById(productSaleDto.getId());
        if (!existingProductSale) {
            throw new NoSuchElementException("Product sale with id " + productSaleDto.getId() + " not found.");
        }
        ProductSale productSale = convertToEntity(productSaleDto);
        productSale = productSaleRepository.save(productSale);
        return convertToDto(productSale);
    }

    public void deleteProductSale(Long id) {
        productSaleRepository.deleteById(id);
    }

    public ProductSaleDto convertToDto(ProductSale productSale) {
        var product = productService.getFinishedProductById(productSale.getProduct().getId());
        var employee = employeeService.getEmployeeById(productSale.getEmployee().getId());
        return ProductSaleDto.builder()
                .id(productSale.getId())
                .product(product)
                .quantity(productSale.getQuantity())
                .cost(productSale.getCost())
                .employee(employee)
                .date(productSale.getDate())
                .build();
    }

    private ProductSale convertToEntity(ProductSaleDto productSaleDto) {
        FinishedProduct product = productService.getFinishedProduct(productSaleDto.getProduct().getId()).get();
        Employee employee = employeeService.getEmployee(productSaleDto.getEmployee().getId()).get();
        return ProductSale.builder()
                .id(productSaleDto.getId())
                .product(product)
                .quantity(productSaleDto.getQuantity())
                .cost(productSaleDto.getCost())
                .employee(employee)
                .date(productSaleDto.getDate())
                .build();
    }
    public int getNumberOfSalesByEmployeeAndMonth(Employee employee, int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        Date start = java.sql.Date.valueOf(startDate);
        Date end = java.sql.Date.valueOf(endDate);

        return productSaleRepository.countByEmployeeAndDateBetween(employee, start, end);
    }

}
