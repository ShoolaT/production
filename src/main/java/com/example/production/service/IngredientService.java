package com.example.production.service;

import com.example.production.dto.IngredientDto;
import com.example.production.model.Ingredient;
import com.example.production.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

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

    public Page<IngredientDto> getIngredients(int page, int size, String sort) {
        var list = ingredientRepository.findAll(PageRequest.of(page, size, Sort.by(sort)));
        return toPage(list.getContent(), PageRequest.of(list.getNumber(), list.getSize(), list.getSort()));
    }
    public Page<IngredientDto> getIngredientsByProductId(Long productId, int page, int size, String sort) {
        // Assuming you have a method in your repository to fetch ingredients by product ID
        var list = ingredientRepository.findByProductId(productId, PageRequest.of(page, size, Sort.by(sort)));
        return toPage(list.getContent(), PageRequest.of(list.getNumber(), list.getSize(), list.getSort()));
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
    public List<IngredientDto> getAllIngredients() {
        List<Ingredient> ingredients = ingredientRepository.findAll();
        return ingredients.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public IngredientDto getIngredientById(Long id) {
        var ingredient = ingredientRepository.findById(id).get();
        return convertToDto(ingredient);
    }
    public Optional<Ingredient> getIngredient(Long id) {
        return Optional.of(ingredientRepository.findById(id).get());
    }

    public IngredientDto saveIngredient(IngredientDto ingredientDto) {
        Ingredient ingredient = convertToEntity(ingredientDto);
        ingredient = ingredientRepository.save(ingredient);
        return convertToDto(ingredient);
    }
    public IngredientDto updateIngredient(IngredientDto ingredientDto) {
        boolean existingIngredient = ingredientRepository.existsById(ingredientDto.getId());
        if(!existingIngredient){
            throw new NoSuchElementException("Ingredient with id " + ingredientDto.getId() + " not found.");
        }
        Ingredient ingredient = convertToEntity(ingredientDto);
        ingredient = ingredientRepository.save(ingredient);
        return convertToDto(ingredient);
    }
    public void deleteIngredient(Long id) {
        ingredientRepository.deleteById(id);
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
//    public List<IngredientDto> getUnusedIngredientsForProduct(Long productId) {
//        // Получить список всех ингредиентов
//        List<Ingredient> allIngredients = ingredientRepository.findAll();
//
//        // Получить список ингредиентов для указанного продукта
//        List<IngredientDto> ingredientsForProduct = getIngredientsForProduct(productId);
//
//        // Из списка всех ингредиентов исключить те, которые уже использованы для указанного продукта
//        List<IngredientDto> unusedIngredients = new ArrayList<>();
//        for (Ingredient ingredient : allIngredients) {
//            boolean isUsed = ingredientsForProduct.stream()
//                    .anyMatch(ing -> ing.getId().equals(ingredient.getId()));
//            if (!isUsed) {
//                unusedIngredients.add(convertToDto(ingredient));
//            }
//        }
//
//        return unusedIngredients;
//    }
public List<IngredientDto> getIngredientsForProduct(Long productId) {
    List<Ingredient> ingredients = ingredientRepository.findByProduct_Id(productId);
    return ingredients.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
}
    public List<IngredientDto> getUnusedIngredientsForProduct(Long productId) {
        List<IngredientDto> allIngredients = getAllIngredients();
        List<IngredientDto> ingredientsForProduct = getIngredientsForProduct(productId);

        // Из списка всех ингредиентов исключить те, которые уже использованы для указанного продукта
        return allIngredients.stream()
                .filter(ingredient -> ingredientsForProduct.stream()
                        .noneMatch(ing -> ing.getId().equals(ingredient.getId())))
                .collect(Collectors.toList());
    }


//    public List<IngredientDto> getIngredientsForProduct(Long productId) {
//        // Получить список ингредиентов для указанного продукта
//        List<Ingredient> ingredients = ingredientRepository.findByProduct_Id(productId);
//
//        // Преобразовать список ингредиентов в список DTO
//        return ingredients.stream()
//                .map(this::convertToDto)
//                .collect(Collectors.toList());
//    }




}
