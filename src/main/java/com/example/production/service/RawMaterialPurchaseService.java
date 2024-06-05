package com.example.production.service;

import com.example.production.dto.RawMaterialPurchaseDto;
import com.example.production.model.Employee;
import com.example.production.model.ProductSale;
import com.example.production.model.RawMaterial;
import com.example.production.model.RawMaterialPurchase;
import com.example.production.repository.RawMaterialPurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class RawMaterialPurchaseService {
    private final RawMaterialPurchaseRepository materialPurchaseRepository;
    private final RawMaterialService rawMaterialService;
    private final EmployeeService employeeService;

    @Transactional
    public List<RawMaterialPurchase> getMaterialPurchases(Date startDate, Date endDate) {
        if (startDate == null && endDate == null) {
            return materialPurchaseRepository.getMaterialPurchasesByDate(null, null);
        } else {
            return materialPurchaseRepository.getMaterialPurchasesByDate(startDate, endDate);
        }
    }

    public RawMaterialPurchaseDto getMaterialPurchaseById(Long id) {
        var materialPurchase = materialPurchaseRepository.findById(id).get();
        return convertToDto(materialPurchase);
    }

    public RawMaterialPurchaseDto saveMaterialPurchase(RawMaterialPurchaseDto materialPurchaseDto) {
        RawMaterialPurchase materialPurchase = convertToEntity(materialPurchaseDto);
        materialPurchase = materialPurchaseRepository.save(materialPurchase);
        return convertToDto(materialPurchase);
    }

    public RawMaterialPurchaseDto updateMaterialPurchase(RawMaterialPurchaseDto materialPurchaseDto) {
        boolean existingMaterial = materialPurchaseRepository.existsById(materialPurchaseDto.getId());
        if (!existingMaterial) {
            throw new NoSuchElementException("Raw material purchase with id " + materialPurchaseDto.getId() + " not found.");
        }
        RawMaterialPurchase materialPurchase = convertToEntity(materialPurchaseDto);
        materialPurchase = materialPurchaseRepository.save(materialPurchase);
        return convertToDto(materialPurchase);
    }

    public void deleteRawMaterialPurchase(Long id) {
        materialPurchaseRepository.deleteById(id);
    }

    public float calculateTotalQuantity(List<RawMaterialPurchase> materialPurchases) {
        float totalQuantity = 0;
        for (RawMaterialPurchase materialPurchase : materialPurchases) {
            totalQuantity += materialPurchase.getQuantity();
        }
        return totalQuantity;
    }

    public float calculateTotalAmount(List<RawMaterialPurchase> materialPurchases) {
        float totalAmount = 0;
        for (RawMaterialPurchase materialPurchase : materialPurchases) {
            totalAmount += materialPurchase.getAmount();
        }
        return totalAmount;
    }
    public RawMaterialPurchaseDto convertToDto(RawMaterialPurchase materialPurchase) {
        var rawMaterial = rawMaterialService.getMaterialById(materialPurchase.getRawMaterial().getId());
        var employee = employeeService.getEmployeeById(materialPurchase.getEmployee().getId());
        return RawMaterialPurchaseDto.builder()
                .id(materialPurchase.getId())
                .rawMaterial(rawMaterial)
                .quantity(materialPurchase.getQuantity())
                .amount(materialPurchase.getAmount())
                .employee(employee)
                .date(materialPurchase.getDate())
                .build();
    }

    private RawMaterialPurchase convertToEntity(RawMaterialPurchaseDto materialPurchaseDto) {
        RawMaterial material = rawMaterialService.getMaterial(materialPurchaseDto.getRawMaterial().getId()).get();
        Employee employee = employeeService.getEmployee(materialPurchaseDto.getEmployee().getId()).get();
        return RawMaterialPurchase.builder()
                .id(materialPurchaseDto.getId())
                .rawMaterial(material)
                .quantity(materialPurchaseDto.getQuantity())
                .amount(materialPurchaseDto.getAmount())
                .employee(employee)
                .date(materialPurchaseDto.getDate())
                .build();
    }
}
