package com.example.production.controller;

import com.example.production.dto.EmployeeDto;
import com.example.production.dto.RawMaterialDto;
import com.example.production.dto.RawMaterialPurchaseDto;
import com.example.production.model.RawMaterialPurchase;
import com.example.production.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayInputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        List<RawMaterialPurchase> materialPurchases = materialPurchaseService.getMaterialPurchases(null, null);

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
    @GetMapping("/all/report")
    public String getProductionsToReport(Model model,
                                         @RequestParam(name = "startDate", required = false) String startDate,
                                         @RequestParam(name = "endDate", required = false) String endDate) {
        List<RawMaterialPurchase> purchases = null; // Устанавливаем значение по умолчанию

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        if (startDate != null && endDate != null) {
            try {
                Date start = formatter.parse(startDate);
                Date end = formatter.parse(endDate);
                purchases = materialPurchaseService.getMaterialPurchases(start, end);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            model.addAttribute("startD", startDate);
            model.addAttribute("endD", endDate);
            model.addAttribute("totalQuantity", materialPurchaseService.calculateTotalQuantity(purchases));
            model.addAttribute("totalAmount", materialPurchaseService.calculateTotalAmount(purchases));
        }

        model.addAttribute("materialPurchases", purchases);

        return "report/materialPurchases";
    }


    @GetMapping("/all/print")
    public ResponseEntity<byte[]> printReport(@RequestParam(name = "startDate", required = false) String startDate,
                                              @RequestParam(name = "endDate", required = false) String endDate) {
        List<RawMaterialPurchase> purchases;
        Date start = null, end = null;
        if (startDate == null || endDate == null) {
            purchases = null;
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            try {
                start = formatter.parse(startDate);
                end = formatter.parse(endDate);
                purchases = materialPurchaseService.getMaterialPurchases(start, end);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;

        String user = employeeService.getEmployeeDetailsByEmail(userDetails.getUsername());

        String[] details = user.split(",");

        Float totalQuantity = null;
        Float totalAmount = null;
        if(purchases != null) {
            totalQuantity = (float) materialPurchaseService.calculateTotalQuantity(purchases);
            totalAmount = (float) materialPurchaseService.calculateTotalAmount(purchases);
        }
        ByteArrayInputStream bis = ExportPdf.materialPurchasesReport(purchases, details, start, end, totalQuantity, totalAmount);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "material_purchases_report.pdf");

        byte[] bytes = new byte[0];
        bytes = new byte[bis.available()];
        bis.read(bytes, 0, bis.available());

        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }


}
