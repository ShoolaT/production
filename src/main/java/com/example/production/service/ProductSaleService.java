package com.example.production.service;

import com.example.production.dto.ProductSaleDto;
import com.example.production.model.Bank;
import com.example.production.model.Employee;
import com.example.production.model.FinishedProduct;
import com.example.production.model.ProductSale;
import com.example.production.repository.ProductSaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional

    public List<ProductSale> getProductSales(Date startDate, Date endDate) {
        if (startDate == null && endDate == null) {
            return productSaleRepository.getSalesByDate(null, null);
        } else {
            return productSaleRepository.getSalesByDate(startDate, endDate);
        }
    }

    public ProductSaleDto getProductSaleById(Long id) {
        var productSale = productSaleRepository.findById(id).get();
        return convertToDto(productSale);
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
    public float calculateTotalQuantity(List<ProductSale> sales) {
        float totalQuantity = 0;
        for (ProductSale productSale : sales) {
            totalQuantity += productSale.getQuantity();
        }
        return totalQuantity;
    }

    public float calculateTotalAmount(List<ProductSale> sales) {
        float totalAmount = 0;
        for (ProductSale productSale : sales) {
            totalAmount += productSale.getAmount();
        }
        return totalAmount;
    }


    public ProductSaleDto convertToDto(ProductSale productSale) {
        var product = productService.getFinishedProductById(productSale.getProduct().getId());
        var employee = employeeService.getEmployeeById(productSale.getEmployee().getId());
        return ProductSaleDto.builder()
                .id(productSale.getId())
                .product(product)
                .quantity(productSale.getQuantity())
                .amount(productSale.getAmount())
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
                .amount(productSaleDto.getAmount())
                .employee(employee)
                .date(productSaleDto.getDate())
                .build();
    }

}
