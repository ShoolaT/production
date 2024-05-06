package com.example.production.controller;

import com.example.production.dto.*;
import com.example.production.service.BudgetService;
import com.example.production.service.EmployeeService;
import com.example.production.service.RawMaterialPurchaseService;
import com.example.production.service.RawMaterialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/materialPurchases")
@RequiredArgsConstructor
@Slf4j
public class RawMaterialPurchaseController {
    private final RawMaterialPurchaseService materialPurchaseService;
    private final RawMaterialService materialService;
    private final EmployeeService employeeService;
    private final BudgetService budgetService;
    private boolean isError;

    @GetMapping("/create")
    public String createShowMaterialPurchase(Model model,
                                             @RequestParam(name = "rawMaterialId", required = false) Long rawMaterialId,
                                             @RequestParam(name = "quantity", required = false) Double quantity,
                                             @RequestParam(name = "amount", required = false) Double amount,
                                             @RequestParam(name = "employeeId", required = false) Long employeeId) {
        List<EmployeeDto> employees = employeeService.getAllEmployees();
        List<RawMaterialDto> materials = materialService.getAllMaterials();

        model.addAttribute("employees", employees);
        model.addAttribute("materials", materials);
        model.addAttribute("materialPurchaseDto", new RawMaterialPurchaseDto());

        if (rawMaterialId != null) {
            RawMaterialDto rawMaterialDto = new RawMaterialDto();
            rawMaterialDto.setId(rawMaterialId);
            model.addAttribute("rawMaterialId", rawMaterialDto);
        }
        if (quantity != null) {
            model.addAttribute("quantity", quantity);
        }
        if (amount != null) {
            model.addAttribute("amount", amount);
        }
        if (employeeId != null) {
            EmployeeDto employeeDto = new EmployeeDto();
            employeeDto.setId(employeeId);
            model.addAttribute("employeeId", employeeDto);
        }
        if (!isError) {
            model.addAttribute("error", "");
        } else {
            model.addAttribute("error", "У вас нехватка бюджета. Ваш бюджет составляет: " + budgetService.getBudget());
            isError = false;
        }

        return "materialPurchases/createMaterialPurchase";
    }


    @PostMapping("/create")
    public String createMaterialPurchase(RawMaterialPurchaseDto materialPurchaseDto, RedirectAttributes redirectAttributes) {
        float requiredAmount = (float) (materialPurchaseDto.getAmount() * materialPurchaseDto.getQuantity());

        if (materialPurchaseDto.getDate() != null) {
            System.out.println(materialPurchaseDto.getDate());
        }

        if (budgetService.checkBudget(requiredAmount)) {
            materialPurchaseService.saveMaterialPurchase(materialPurchaseDto);

            budgetService.decreaseBudget(requiredAmount);

            materialService.increaseQuantityAndAmount(materialPurchaseDto.getRawMaterial().getId(), materialPurchaseDto.getQuantity(), requiredAmount);
            isError = false;
            return "redirect:/materialPurchases/all";
        } else {
            redirectAttributes.addAttribute("rawMaterialId", materialPurchaseDto.getRawMaterial().getId());
            redirectAttributes.addAttribute("quantity", materialPurchaseDto.getQuantity());
            redirectAttributes.addAttribute("amount", materialPurchaseDto.getAmount());
            redirectAttributes.addAttribute("date", materialPurchaseDto.getDate());
            redirectAttributes.addAttribute("employeeId", materialPurchaseDto.getEmployee().getId());

            isError = true;
            return "redirect:/materialPurchases/create";
        }
    }

    @GetMapping("/all")
    public String getAllMaterialPurchases(Model model,
                                          @RequestParam(name = "sort", defaultValue = "id") String sortCriteria) {
        List<EmployeeDto> employees = employeeService.getAllEmployees();
        List<RawMaterialDto> materials = materialService.getAllMaterials();

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#0.00", symbols);

        String budget = df.format(budgetService.getBudget());
        model.addAttribute("budget", budget);

        model.addAttribute("employees", employees);
        model.addAttribute("materials", materials);
        List<RawMaterialPurchaseDto> materialPurchases = materialPurchaseService.getMaterialPurchases(0, 9, sortCriteria).getContent();

        model.addAttribute("materialPurchases", materialPurchases);

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        model.addAttribute("roles", roles);
        return "materialPurchases/allMaterialPurchases";
    }

    @GetMapping("/edit/{id}")
    public String editMaterialPurchase(@PathVariable Long id, Model model) {
        RawMaterialPurchaseDto materialPurchaseDto = materialPurchaseService.getMaterialPurchaseById(id);
        if (materialPurchaseDto != null) {
            model.addAttribute("employees", employeeService.getAllEmployees());
            model.addAttribute("materials", materialService.getAllMaterials());
            model.addAttribute("materialPurchaseDto", materialPurchaseDto);

            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
            symbols.setDecimalSeparator('.');
            DecimalFormat df = new DecimalFormat("#0.00", symbols);

            String formattedQuantity = df.format(materialPurchaseDto.getQuantity());
            model.addAttribute("formattedQuantity", formattedQuantity);

            String formattedAmount = df.format(materialPurchaseDto.getAmount());
            model.addAttribute("formattedAmount", formattedAmount);
        } else {
            new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return "materialPurchases/updateMaterialPurchase";
    }

    @PostMapping("/{id}/edit")
    public String updateMaterialPurchase(@PathVariable Long id, @ModelAttribute RawMaterialPurchaseDto materialPurchaseDto) {
        materialPurchaseDto.setId(id);
        materialPurchaseService.updateMaterialPurchase(materialPurchaseDto);
        return "redirect:/materialPurchases/all";
    }

    @GetMapping("/{id}/delete/confirmed")
    public String confirmDelete(@PathVariable Long id) {
        materialPurchaseService.deleteRawMaterialPurchase(id);
        return "redirect:/materialPurchases/all";
    }


}
