package com.example.production.controller;

import com.example.production.dto.EmployeeDto;
import com.example.production.dto.FinishedProductDto;
import com.example.production.dto.ProductSaleDto;
import com.example.production.service.BudgetService;
import com.example.production.service.EmployeeService;
import com.example.production.service.FinishedProductService;
import com.example.production.service.ProductSaleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/productSales")
@RequiredArgsConstructor
@Slf4j
public class ProductSaleController {
    private final ProductSaleService productSaleService;
    private final FinishedProductService productService;
    private final EmployeeService employeeService;
    private final BudgetService budgetService;
    private boolean isErrorQuantity;
//    private boolean isErrorCost;

    @GetMapping("/create")
    public String createProductSale(Model model,
                                    @RequestParam(name = "productId", required = false) Long productId,
                                    @RequestParam(name = "quantity", required = false) Float quantity,
                                    @RequestParam(name = "cost", required = false) Float cost,
                                    @RequestParam(name = "employeeId", required = false) Long employeeId) {
        List<EmployeeDto> employees = employeeService.getAllEmployees();
        List<FinishedProductDto> products = productService.getAllFinishedProducts();

        model.addAttribute("employees", employees);
        model.addAttribute("products", products);
        model.addAttribute("productSaleDto", new ProductSaleDto());

        if (productId != null) {
            FinishedProductDto finishedProductDto = new FinishedProductDto();
            finishedProductDto.setId(productId);
            model.addAttribute("productId", finishedProductDto);
        }
        if (quantity != null) {
            model.addAttribute("quantity", quantity);
        }
        if (cost != null) {
            model.addAttribute("cost", cost);
        }
        if (employeeId != null) {
            EmployeeDto employeeDto = new EmployeeDto();
            employeeDto.setId(employeeId);
            model.addAttribute("employeeId", employeeDto);
        }
        if (!isErrorQuantity) {
            model.addAttribute("errorQuantity", "");
        } else {
            model.addAttribute("errorQuantity", "There is no such quantity of product in stock.");
            isErrorQuantity = false;
        }

        return "productSales/createProductSale";
    }


    @PostMapping("/create")
    public String createProductSale(ProductSaleDto productSaleDto, RedirectAttributes redirectAttributes) {

        if (productService.checkQuantity(productSaleDto.getProduct().getId(), productSaleDto.getQuantity()) && productSaleDto.getQuantity() > 0) {

            float costProduct = productService.costForFinishedProduct(productSaleDto.getProduct().getId());//себестоимость продукта
            float minCost = costProduct + costProduct * budgetService.getPercent() / 100;//стоимость продукта
            productSaleDto.setAmount(minCost * productSaleDto.getQuantity());
            float totalCost = productSaleDto.getAmount();
            budgetService.increaseBudget(totalCost);
            productService.decreaseFinishedProduct(productSaleDto.getProduct().getId(), productSaleDto.getQuantity(), costProduct);
            productSaleService.saveProductSale(productSaleDto);

            isErrorQuantity = false;
            return "redirect:/productSales/all";
        } else {
            redirectAttributes.addAttribute("productId", productSaleDto.getProduct().getId());
            redirectAttributes.addAttribute("quantity", productSaleDto.getQuantity());
            redirectAttributes.addAttribute("cost", productSaleDto.getAmount());
            redirectAttributes.addAttribute("date", productSaleDto.getDate());
            redirectAttributes.addAttribute("employeeId", productSaleDto.getEmployee().getId());

            isErrorQuantity = true;
            return "redirect:/productSales/create";
        }
    }

    @GetMapping("/all")
    public String getAllProductSales(Model model,
                                     @RequestParam(name = "sort", defaultValue = "id") String sortCriteria) {
        List<EmployeeDto> employees = employeeService.getAllEmployees();
        List<FinishedProductDto> products = productService.getAllFinishedProducts();

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#0.00", symbols);

        String budget = df.format(budgetService.getBudget());
        model.addAttribute("budget", budget);

        String budgetPercent = df.format(budgetService.getPercent());
        model.addAttribute("budgetPercent", budgetPercent);

        model.addAttribute("employees", employees);
        model.addAttribute("products", products);
        List<ProductSaleDto> productSales = productSaleService.getProductSales(0, 9, sortCriteria).getContent();

        model.addAttribute("productSales", productSales);

        return "productSales/allProductSales";
    }

    @GetMapping("/edit/{id}")
    public String editProductSale(@PathVariable Long id, Model model) {
        ProductSaleDto productSaleDto = productSaleService.getProductSaleById(id);
        if (productSaleDto != null) {
            model.addAttribute("employees", employeeService.getAllEmployees());
            model.addAttribute("products", productService.getAllFinishedProducts());
            model.addAttribute("productSaleDto", productSaleDto);

            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
            symbols.setDecimalSeparator('.');
            DecimalFormat df = new DecimalFormat("#0.00", symbols);

            String formattedQuantity = df.format(productSaleDto.getQuantity());
            model.addAttribute("formattedQuantity", formattedQuantity);

            String formattedCost = df.format(productSaleDto.getAmount());
            model.addAttribute("formattedCost", formattedCost);
        } else {
            new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return "productSales/updateProductSale";
    }

    @PostMapping("/{id}/edit")
    public String updateProductSale(@PathVariable Long id, @ModelAttribute ProductSaleDto productSaleDto) {
        productSaleDto.setId(id);
        productSaleService.updateProductSale(productSaleDto);
        return "redirect:/productSales/all";
    }

    @GetMapping("/{id}/delete/confirmed")
    public String confirmDelete(@PathVariable Long id) {
        productSaleService.deleteProductSale(id);
        return "redirect:/productSales/all";
    }


}
