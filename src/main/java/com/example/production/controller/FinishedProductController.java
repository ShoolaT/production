package com.example.production.controller;

import com.example.production.dto.FinishedProductDto;
import com.example.production.dto.UnitOfMeasurementDto;
import com.example.production.service.FinishedProductService;
import com.example.production.service.UnitsOfMeasurementService;
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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
public class FinishedProductController {
    private final FinishedProductService productService;
    private final UnitsOfMeasurementService measurementService;
    @GetMapping("/create")
    public String createShowProduct(Model model) {
        List<UnitOfMeasurementDto> measurements = measurementService.getAllMeasurements();
        model.addAttribute("measurements", measurements);
        return "products/createProduct";
    }
    @PostMapping("/create")
    public String createProduct(@ModelAttribute FinishedProductDto productDto) {
        productService.saveFinishedProduct(productDto);
        return "redirect:/products/all";
    }

    @GetMapping("/all")
    public String getAllProducts(Model model,
                                  @RequestParam(name = "sort", defaultValue = "id") String sortCriteria) {
        var products = productService.getFinishedProducts();
        model.addAttribute("products", products);
        model.addAttribute("measurements", measurementService.getAllMeasurements());

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        model.addAttribute("roles", roles);
        return "products/allProducts";
    }
    @PostMapping("/{id}/edit")
    public String updateProduct(@PathVariable Long id, @ModelAttribute FinishedProductDto productDto) {
        productDto.setId(id);
        productService.updateProduct(productDto);
        return "redirect:/products/all";
    }
    @GetMapping("/{id}/edit")
    public String updateShowProduct(@PathVariable Long id, Model model) {
        FinishedProductDto productDto = productService.getFinishedProductById(id);
        if (productDto != null) {
            model.addAttribute("productDto", productDto);
            model.addAttribute("measurements",measurementService.getAllMeasurements());

            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
            symbols.setDecimalSeparator('.');
            DecimalFormat df = new DecimalFormat("#0.00", symbols);

            String formattedQuantity = df.format(productDto.getQuantity());
            model.addAttribute("formattedQuantity", formattedQuantity);

            String formattedAmount = df.format(productDto.getAmount());
            model.addAttribute("formattedAmount", formattedAmount);
        } else {
            new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return "products/updateProduct";
    }

    @GetMapping("/{id}/delete/confirmed")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteFinishedProduct(id);
        return "redirect:/products/all";
    }
}
