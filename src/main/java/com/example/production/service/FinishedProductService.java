package com.example.production.service;

import com.example.production.dto.FinishedProductDto;
import com.example.production.model.FinishedProduct;
import com.example.production.model.UnitsOfMeasurement;
import com.example.production.repository.FinishedProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FinishedProductService {
    private final FinishedProductRepository finishedProductRepository;
    private final UnitsOfMeasurementService unitsOfMeasurementService;

    public Page<FinishedProductDto> getFinishedProducts(int page, int size, String sort) {
        var list = finishedProductRepository.findAll(PageRequest.of(page, size, Sort.by(sort)));
        return toPage(list.getContent(), PageRequest.of(list.getNumber(), list.getSize(), list.getSort()));
    }

    private Page<FinishedProductDto> toPage(List<FinishedProduct> products, Pageable pageable) {
        var list = products.stream()
                .map(this::convertToDto)
                .toList();
        if (pageable.getOffset() >= list.size()) {
            return Page.empty();
        }
        int startIndex = (int) pageable.getOffset();
        int endIndex = (int) ((pageable.getOffset() + pageable.getPageSize()) > list.size() ?
                list.size() :
                pageable.getOffset() + pageable.getPageSize());
        List<FinishedProductDto> subList = list.subList(startIndex, endIndex);
        return new PageImpl<>(subList, pageable, list.size());
    }
    public List<FinishedProductDto> getAllFinishedProducts() {
        List<FinishedProduct> productDto = finishedProductRepository.findAll();
        return productDto.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public FinishedProductDto getFinishedProductById(Long id) {
        var finishedProduct = finishedProductRepository.findById(id).get();
        return convertToDto(finishedProduct);
    }
    public Optional<FinishedProduct> getFinishedProduct(Long id) {
        return Optional.of(finishedProductRepository.findById(id).get());
    }

    public FinishedProductDto saveFinishedProduct(FinishedProductDto productDto) {
        FinishedProduct finishedProduct = convertToEntity(productDto);
        finishedProduct = finishedProductRepository.save(finishedProduct);
        return convertToDto(finishedProduct);
    }
    public FinishedProductDto updateProduct(FinishedProductDto productDto) {
        boolean existingProduct = finishedProductRepository.existsById(productDto.getId());
        if(!existingProduct){
            throw new NoSuchElementException("Product with name " + productDto.getName() + " not found.");
        }
        FinishedProduct product = convertToEntity(productDto);
        product = finishedProductRepository.save(product);
        return convertToDto(product);
    }

    public void deleteFinishedProduct(Long id) {
        finishedProductRepository.deleteById(id);
    }

    public FinishedProductDto getFirstFinishedProduct() {
        List<FinishedProduct> products = finishedProductRepository.findAll(PageRequest.of(0, 1)).getContent();

        if (products.isEmpty()) {
            throw new NoSuchElementException("No finished products found.");
        }

        return convertToDto(products.get(0));
    }

    public FinishedProductDto convertToDto(FinishedProduct finishedProduct) {
        var measurement = unitsOfMeasurementService.getMeasurementById(finishedProduct.getUnitsOfMeasurement().getId());
        return FinishedProductDto.builder()
                .id(finishedProduct.getId())
                .name(finishedProduct.getName())
                .unitsOfMeasurement(measurement)
                .quantity(finishedProduct.getQuantity())
                .amount(finishedProduct.getAmount())
                .build();
    }

    private FinishedProduct convertToEntity(FinishedProductDto finishedProductDto) {
        UnitsOfMeasurement measurement = unitsOfMeasurementService.getMeasurement(finishedProductDto.getUnitsOfMeasurement().getId()).get();
        return FinishedProduct.builder()
                .id(finishedProductDto.getId())
                .name(finishedProductDto.getName())
                .unitsOfMeasurement(measurement)
                .quantity(finishedProductDto.getQuantity())
                .amount(finishedProductDto.getAmount())
                .build();
    }
}
