package com.example.production.service;

import com.example.production.dto.IngredientDto;
import com.example.production.dto.ProductProductionDto;
import com.example.production.model.Employee;
import com.example.production.model.FinishedProduct;
import com.example.production.model.ProductProduction;
import com.example.production.repository.ProductProductionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductProductionService {
    private final ProductProductionRepository productionRepository;
    private final FinishedProductService productService;
    private final EmployeeService employeeService;
    private final IngredientService ingredientService;
    private final RawMaterialService rawMaterialService;

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

    public List<ProductProductionDto> getAllProductProductions() {
        List<ProductProduction> productions = productionRepository.findAll();
        return productions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ProductProductionDto getProductProductionById(Long id) {
        var production = productionRepository.findById(id).get();
        return convertToDto(production);
    }

    public Optional<ProductProduction> getProductProduction(Long id) {
        return Optional.of(productionRepository.findById(id).get());
    }

//    public ProductProductionDto saveProductProduction(ProductProductionDto productionDto) {
//        ProductProduction production = convertToEntity(productionDto);
//        production = productionRepository.save(production);
//
//        float producedQuantity = productionDto.getQuantity();
//        Long productId = productionDto.getProduct().getId();
//        productService.increaseQuantity(productId, producedQuantity);
//        return convertToDto(production);
//    }
public ProductProductionDto saveProductProduction(ProductProductionDto productionDto) {
    List<IngredientDto> ingredients = ingredientService.getIngredientsForProduct(productionDto.getProduct().getId());

    float totalCostOfRawMaterials = 0;
    for (IngredientDto ingredient : ingredients) {
        float requiredQuantity = ingredient.getQuantity() * productionDto.getQuantity();
        float costPerUnit = rawMaterialService.costForRawMaterial(ingredient.getRawMaterial().getId());
        totalCostOfRawMaterials += requiredQuantity * costPerUnit;
    }

    ProductProduction production = convertToEntity(productionDto);
    production = productionRepository.save(production);


    float producedQuantity = productionDto.getQuantity();
    Long productId = productionDto.getProduct().getId();
    productService.increaseQuantity(productId, producedQuantity);


    productService.updateAmount(productId, totalCostOfRawMaterials);

    return convertToDto(production);
}

    public ProductProductionDto updateProductProduction(ProductProductionDto productionDto) {
        boolean existingProduction = productionRepository.existsById(productionDto.getId());
        if (!existingProduction) {
            throw new NoSuchElementException("Product production with id " + productionDto.getId() + " not found.");
        }
        ProductProduction production = convertToEntity(productionDto);
        production = productionRepository.save(production);
        return convertToDto(production);
    }

    public void deleteProductProduction(Long id) {
        productionRepository.deleteById(id);
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
