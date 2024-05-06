package com.example.production.controller;

import com.example.production.dto.RawMaterialDto;
import com.example.production.dto.UnitOfMeasurementDto;
import com.example.production.service.RawMaterialService;
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
@RequestMapping("/materials")
@RequiredArgsConstructor
@Slf4j
public class RawMaterialController {
    private final RawMaterialService rawMaterialService;
    private final UnitsOfMeasurementService measurementService;
    @GetMapping("/create")
    public String createShowRawMaterial(Model model) {
        List<UnitOfMeasurementDto> measurements = measurementService.getAllMeasurements();
        model.addAttribute("measurements", measurements);
        return "materials/createRawMaterial";
    }
    @PostMapping("/create")
    public String createRawMaterial(@ModelAttribute RawMaterialDto materialDto) {
        rawMaterialService.saveMaterial(materialDto);
        return "redirect:/materials/all";
    }

    @GetMapping("/all")
    public String getAllRawMaterials(Model model,
                                  @RequestParam(name = "sort", defaultValue = "id") String sortCriteria) {
        var materials = rawMaterialService.getMaterials(0,9,sortCriteria);
        model.addAttribute("materials", materials);

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        model.addAttribute("roles", roles);
        return "materials/allRawMaterials";
    }
    @PostMapping("/{id}/edit")
    public String updateRawMaterial(@PathVariable Long id, @ModelAttribute RawMaterialDto rawMaterialDto) {
        rawMaterialDto.setId(id);

        rawMaterialService.updateMaterial(rawMaterialDto);
        return "redirect:/materials/all";
    }
    @GetMapping("/{id}/edit")
    public String updateShowRawMaterial(@PathVariable Long id, Model model) {
        RawMaterialDto materialDto = rawMaterialService.getMaterialById(id);
        if (materialDto != null) {
            model.addAttribute("materialDto", materialDto);
            model.addAttribute("measurements", measurementService.getAllMeasurements());

            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
            symbols.setDecimalSeparator('.');
            DecimalFormat df = new DecimalFormat("#0.00", symbols);

            String formattedQuantity = df.format(materialDto.getQuantity());
            model.addAttribute("formattedQuantity", formattedQuantity);

            String formattedAmount = df.format(materialDto.getAmount());
            model.addAttribute("formattedAmount", formattedAmount);
        } else {
            new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return "materials/updateRawMaterial";
    }

    @GetMapping("/{id}/delete/confirmed")
    public String deleteRawMaterial(@PathVariable Long id) {
        rawMaterialService.deleteMaterial(id);
        return "redirect:/materials/all";
    }
}
