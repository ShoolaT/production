package com.example.production.controller;

import com.example.production.dto.EmployeeDto;
import com.example.production.dto.FinishedProductDto;
import com.example.production.dto.ProductProductionDto;
import com.example.production.model.ProductProduction;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/productProductions")
@RequiredArgsConstructor
@Slf4j
public class ProductProductionController {
    private final ProductProductionService productProductionService;
    private final FinishedProductService productService;
    private final EmployeeService employeeService;
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
    @Transactional
    public String createProductProduction(ProductProductionDto productionDto, RedirectAttributes redirectAttributes) {
        if (rawMaterialService.checkRawMaterial(productionDto.getProduct().getId(), productionDto.getQuantity())) {

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
    public String getAllProductProductions(Model model,
                                           @RequestParam(name = "sort", defaultValue = "id") String sortCriteria) {
        List<EmployeeDto> employees = employeeService.getAllEmployees();
        List<FinishedProductDto> products = productService.getAllFinishedProducts();

        model.addAttribute("employees", employees);
        model.addAttribute("products", products);
        List<ProductProduction> productions = productProductionService.getProductProduction(null, null);

        model.addAttribute("productions", productions);

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        model.addAttribute("roles", roles);
        return "productions/allProductProductions";
    }

    @GetMapping("/all/report")
    public String getProductionsToReport(Model model,
                                         @RequestParam(name = "startDate", required = false) String startDate,
                                         @RequestParam(name = "endDate", required = false) String endDate) {
        List<ProductProduction> productions = null;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        if (startDate != null && endDate != null) {
            try {
                Date start = formatter.parse(startDate);
                Date end = formatter.parse(endDate);
                productions = productProductionService.getProductProduction(start, end);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            model.addAttribute("startD", startDate);
            model.addAttribute("endD", endDate);
            model.addAttribute("total", productProductionService.calculateTotalQuantity(productions));
        }

        model.addAttribute("productions", productions);

        return "report/productProductions";
    }


    @GetMapping("/all/print")
    public ResponseEntity<byte[]> printReport(@RequestParam(name = "startDate", required = false) String startDate,
                                              @RequestParam(name = "endDate", required = false) String endDate) {
        List<ProductProduction> productions;
        Date start = null, end = null;
        if (startDate == null || endDate == null) {
            productions = null;
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            try {
                start = formatter.parse(startDate);
                end = formatter.parse(endDate);
                productions = productProductionService.getProductProduction(start, end);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;

        String user = employeeService.getEmployeeDetailsByEmail(userDetails.getUsername());

        String[] details = user.split(",");

        Float total= null;
        if(productions != null) {
            total = (float) productProductionService.calculateTotalQuantity(productions);
        }

        ByteArrayInputStream bis = ExportPdf.productProductionReport(productions, details, start, end, total);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "product_production_report.pdf");

        byte[] bytes = new byte[0];
        bytes = new byte[bis.available()];
        bis.read(bytes, 0, bis.available());

        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }


}
