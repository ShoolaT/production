package com.example.production.service;

import com.example.production.dto.IngredientDto;
import com.example.production.model.Ingredient;
import com.example.production.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientRepository ingredientRepository;
    private final FinishedProductService productService;
    private final RawMaterialService materialService;

    public IngredientDto saveIngredient(IngredientDto ingredientDto) {
        Long productId = ingredientDto.getProduct().getId();
        Long rawMaterialId = ingredientDto.getRawMaterial().getId();
        float quantity = ingredientDto.getQuantity();

        Long id = ingredientRepository.createIngredient(productId, rawMaterialId, quantity);
        ingredientDto.setId(id);

        return ingredientDto;
    }
    @Transactional
    public List<IngredientDto> getIngredients() {
        var list = ingredientRepository.getIngredients();
        return list.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    @Transactional
    public List<IngredientDto> getIngredientsByProductId(Long productId) {
        var list = ingredientRepository.findByProductId(productId);
        return list.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    private Page<IngredientDto> toPage(List<Ingredient> ingredients, Pageable pageable) {
        var list = ingredients.stream()
                .map(this::convertToDto)
                .toList();
        if (pageable.getOffset() >= list.size()) {
            return Page.empty();
        }
        int startIndex = (int) pageable.getOffset();
        int endIndex = (int) ((pageable.getOffset() + pageable.getPageSize()) > list.size() ?
                list.size() :
                pageable.getOffset() + pageable.getPageSize());
        List<IngredientDto> subList = list.subList(startIndex, endIndex);
        return new PageImpl<>(subList, pageable, list.size());
    }

    public IngredientDto getIngredientById(Long id) {
        var ingredient = ingredientRepository.findById(id).get();
        return convertToDto(ingredient);
    }

    public IngredientDto updateIngredient(IngredientDto ingredientDto) {
        Long ingredientId = ingredientDto.getId();
        Long productId = ingredientDto.getProduct().getId();
        Long rawMaterialId = ingredientDto.getRawMaterial().getId();
        float quantity = ingredientDto.getQuantity();

        boolean existingIngredient = ingredientRepository.existsById(ingredientId);
        if (!existingIngredient) {
            throw new NoSuchElementException("Ingredient with id " + ingredientId + " not found.");
        }
        ingredientRepository.updateIngredient(ingredientId, productId, rawMaterialId, quantity);

        return ingredientDto;
    }

    public void deleteIngredient(Long id) {
        ingredientRepository.deleteIngredient(id);
    }

    public IngredientDto convertToDto(Ingredient ingredient) {
        var product = productService.getFinishedProductById(ingredient.getProduct().getId());
        var material = materialService.getMaterialById(ingredient.getRawMaterial().getId());
        return IngredientDto.builder()
                .id(ingredient.getId())
                .product(product)
                .rawMaterial(material)
                .quantity(ingredient.getQuantity())
                .build();
    }

    private Ingredient convertToEntity(IngredientDto ingredientDto) {
        var product = productService.getFinishedProduct(ingredientDto.getProduct().getId()).get();
        var material = materialService.getMaterial(ingredientDto.getRawMaterial().getId()).get();
        return Ingredient.builder()
                .id(ingredientDto.getId())
                .product(product)
                .rawMaterial(material)
                .quantity(ingredientDto.getQuantity())
                .build();
    }

    public boolean existsIngredientForProduct(Long productId, Long materialId) {
        return ingredientRepository.existsByProduct_IdAndRawMaterial_Id(productId, materialId);
    }

    public Long getExistingIngredientId(Long productId, Long rawMaterialId) {
        return ingredientRepository.findIdByProductIdAndRawMaterialId(productId, rawMaterialId);
    }


    public List<IngredientDto> getIngredientsForProduct(Long productId) {
        List<Ingredient> ingredients = ingredientRepository.findByProduct_Id(productId);
        return ingredients.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


}
