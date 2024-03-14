package com.example.production.controller;

import com.example.production.dto.EmployeeDto;
import com.example.production.dto.FinishedProductDto;
import com.example.production.dto.IngredientDto;
import com.example.production.dto.ProductProductionDto;
import com.example.production.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/productProductions")
@RequiredArgsConstructor
@Slf4j
public class ProductProductionController {
    private final ProductProductionService productProductionService;
    private final FinishedProductService productService;
    private final EmployeeService employeeService;
    private final IngredientService ingredientService;
    private final RawMaterialService rawMaterialService;
    private boolean isErrorQuantity;

    @GetMapping("/create")
    public String createProductProduction(Model model,
                                          @RequestParam(name = "productId", required = false) Long productId,
                                          @RequestParam(name = "quantity", required = false) Float quantity,
                                          @RequestParam(name = "employeeId", required = false) Long employeeId) {
        List<EmployeeDto> employees = employeeService.getAllEmployees();
        List<FinishedProductDto> products = productService.getAllFinishedProducts();

        model.addAttribute("employees", employees);
        model.addAttribute("products", products);
        model.addAttribute("productProductionDto", new ProductProductionDto());

        if (productId != null) {
            FinishedProductDto finishedProductDto = new FinishedProductDto();
            finishedProductDto.setId(productId);
            model.addAttribute("productId", finishedProductDto);
        }
        if (quantity != null) {
            model.addAttribute("quantity", quantity);
        }
        if (employeeId != null) {
            EmployeeDto employeeDto = new EmployeeDto();
            employeeDto.setId(employeeId);
            model.addAttribute("employeeId", employeeDto);
        }
        if (!isErrorQuantity) {
            model.addAttribute("errorQuantity", "");
        } else {
            model.addAttribute("errorQuantity", "There is no such quantity of raw material in stock.");
            isErrorQuantity = false;
        }

        return "productions/createProductProduction";
    }


    @PostMapping("/create")
    public String createProductProduction(ProductProductionDto productionDto, RedirectAttributes redirectAttributes) {
        List<IngredientDto> ingredients = ingredientService.getIngredientsForProduct(productionDto.getProduct().getId());

        boolean isEnoughRawMaterial = true;

        for (IngredientDto ingredient : ingredients) {
            float requiredQuantity = ingredient.getQuantity() * productionDto.getQuantity();
            if (!rawMaterialService.checkQuantity(ingredient.getRawMaterial().getId(), requiredQuantity)) {
                isEnoughRawMaterial = false;
                break;
            }
        }

        if (isEnoughRawMaterial) {
            for (IngredientDto ingredient : ingredients) {
                float requiredQuantity = ingredient.getQuantity() * productionDto.getQuantity();
                rawMaterialService.decreaseQuantity(ingredient.getRawMaterial().getId(), requiredQuantity);
            }

            productProductionService.saveProductProduction(productionDto);
            return "redirect:/productProductions/all";
        } else {
            redirectAttributes.addAttribute("productId", productionDto.getProduct().getId());
            redirectAttributes.addAttribute("quantity", productionDto.getQuantity());
            redirectAttributes.addAttribute("date", productionDto.getDate());
            redirectAttributes.addAttribute("employeeId", productionDto.getEmployee().getId());

            isErrorQuantity = true;
            return "redirect:/productProductions/create";
        }
    }

    @GetMapping("/all")
    public String getAllProductSales(Model model,
                                     @RequestParam(name = "sort", defaultValue = "id") String sortCriteria) {
        List<EmployeeDto> employees = employeeService.getAllEmployees();
        List<FinishedProductDto> products = productService.getAllFinishedProducts();

        model.addAttribute("employees", employees);
        model.addAttribute("products", products);
        List<ProductProductionDto> productions = productProductionService.getProductProduction(0, 9, sortCriteria).getContent();

        model.addAttribute("productions", productions);

        return "productions/allProductProductions";
    }
}
