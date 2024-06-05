package com.example.production.controller;

import com.example.production.dto.FinishedProductDto;
import com.example.production.dto.IngredientDto;
import com.example.production.dto.RawMaterialDto;
import com.example.production.service.FinishedProductService;
import com.example.production.service.IngredientService;
import com.example.production.service.RawMaterialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/ingredients")
@RequiredArgsConstructor
@Slf4j
public class IngredientsController {
    private final IngredientService ingredientService;
    private final FinishedProductService productService;
    private final RawMaterialService rawMaterialService;

    @GetMapping("/create")
    public String createShowIngredient(Model model,
                                       @RequestParam(name = "productId", required = false) Long selectedProductId) {
        List<FinishedProductDto> products = productService.getAllFinishedProducts();
        List<RawMaterialDto> materials = rawMaterialService.getAllMaterials();

        model.addAttribute("products", products);
        model.addAttribute("materials", materials);

        if (selectedProductId != null) {
            FinishedProductDto selectedProduct = productService.getFinishedProductById(selectedProductId);
            if (selectedProduct != null) {
                model.addAttribute("selectedProduct", selectedProduct);
            }
        }
        model.addAttribute("ingredientExists", false);

        model.addAttribute("ingredientDto", new IngredientDto());

        return "ingredients/createIngredient";
    }

    @PostMapping("/create")
    public String createIngredient(@ModelAttribute IngredientDto ingredientDto, Model model, RedirectAttributes redirectAttributes) {
        Long productId = ingredientDto.getProduct().getId();

        if (ingredientService.existsIngredientForProduct(productId, ingredientDto.getRawMaterial().getId())) {
            FinishedProductDto selectedProduct = productService.getFinishedProductById(productId);

            model.addAttribute("products", productService.getAllFinishedProducts());
            model.addAttribute("materials", rawMaterialService.getAllMaterials());
            model.addAttribute("ingredientExists", true);
            model.addAttribute("message", "Such raw materials for this product already exist");

            model.addAttribute("selectedProduct", selectedProduct);
            return "ingredients/createIngredient";
//            Long existingIngredient = ingredientService.getExistingIngredientId(productId, ingredientDto.getRawMaterial().getId());
//
//            redirectAttributes.addAttribute("id", existingIngredient);
//            redirectAttributes.addAttribute("productId", productId);
//            redirectAttributes.addFlashAttribute("message", "Так как ингредиент был уже создан, вас перенаправило на страницу редактирования этого ингредиента.");
//
//            return "redirect:/ingredients/{id}/edit";
        }

        ingredientService.saveIngredient(ingredientDto);
        return "redirect:/ingredients/all?productId=" + productId;
    }

    @GetMapping("/all")
    public String getAllIngredients(Model model,
                                    @RequestParam(name = "sort", defaultValue = "id") String sortCriteria,
                                    @RequestParam(name = "productId", required = false) Long productId) {
        List<FinishedProductDto> products = productService.getAllFinishedProducts();
        List<RawMaterialDto> materials = rawMaterialService.getAllMaterials();
        model.addAttribute("products", products);
        model.addAttribute("materials", materials);

        List<IngredientDto> ingredients;
        if (productId != null) {
            ingredients = ingredientService.getIngredientsByProductId(productId);
        } else {
            ingredients = ingredientService.getIngredients();
        }

        model.addAttribute("ingredients", ingredients);
        model.addAttribute("selectedProductId", productId != null ? productId : productService.getFirstFinishedProduct().getId());

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        model.addAttribute("roles", roles);
        return "ingredients/allIngredients";
    }

    @PostMapping("/{id}/edit")
    public String updateIngredient(@PathVariable Long id, @ModelAttribute IngredientDto ingredientDto, RedirectAttributes redirectAttributes) {
        ingredientDto.setId(id);
        ingredientService.updateIngredient(ingredientDto);

        redirectAttributes.addAttribute("sort", "id");
        redirectAttributes.addAttribute("productId", ingredientDto.getProduct().getId());
        return "redirect:/ingredients/all";
    }

    @GetMapping("/{id}/edit")
    public String updateShowIngredient(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        IngredientDto ingredientDto = ingredientService.getIngredientById(id);
        List<FinishedProductDto> products = productService.getAllFinishedProducts();
        List<RawMaterialDto> materials = rawMaterialService.getAllMaterials();

        if (ingredientDto != null) {
            model.addAttribute("ingredient", ingredientDto);
            model.addAttribute("products", products);
            model.addAttribute("materials", materials);
            model.addAttribute("selectedProductId", ingredientDto.getProduct().getId());

            String message = (String) redirectAttributes.getFlashAttributes().get("message");
            if (message != null) {
                model.addAttribute("message", message);
            }
        } else {
            new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return "ingredients/updateIngredient";
    }


    @GetMapping("/{id}/delete")
    public String deleteIngredient(@PathVariable Long id, @RequestParam(name = "productId", required = false) Long productId) {
        ingredientService.deleteIngredient(id);
        return "redirect:/ingredients/all?productId=" + (productId != null ? productId : productService.getFirstFinishedProduct().getId());
    }

    @GetMapping("/{id}/delete/confirmed")
    public String confirmDelete(@PathVariable Long id, @RequestParam(name = "productId", required = false) Long productId) {
        ingredientService.deleteIngredient(id);
        return "redirect:/ingredients/all?productId=" + (productId != null ? productId : productService.getFirstFinishedProduct().getId());
    }

}
