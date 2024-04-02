package com.example.production.service;

import com.example.production.dto.RawMaterialPurchaseDto;
import com.example.production.model.Employee;
import com.example.production.model.RawMaterial;
import com.example.production.model.RawMaterialPurchase;
import com.example.production.repository.RawMaterialPurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RawMaterialPurchaseService {
    private final RawMaterialPurchaseRepository materialPurchaseRepository;
    private final RawMaterialService rawMaterialService;
    private final EmployeeService employeeService;

    public Page<RawMaterialPurchaseDto> getMaterialPurchases(int page, int size, String sort) {
        var list = materialPurchaseRepository.findAll(PageRequest.of(page, size, Sort.by(sort)));
        return toPage(list.getContent(), PageRequest.of(list.getNumber(), list.getSize(), list.getSort()));
    }

    private Page<RawMaterialPurchaseDto> toPage(List<RawMaterialPurchase> materialPurchases, Pageable pageable) {
        var list = materialPurchases.stream()
                .map(this::convertToDto)
                .toList();
        if (pageable.getOffset() >= list.size()) {
            return Page.empty();
        }
        int startIndex = (int) pageable.getOffset();
        int endIndex = (int) ((pageable.getOffset() + pageable.getPageSize()) > list.size() ?
                list.size() :
                pageable.getOffset() + pageable.getPageSize());
        List<RawMaterialPurchaseDto> subList = list.subList(startIndex, endIndex);
        return new PageImpl<>(subList, pageable, list.size());
    }

    public List<RawMaterialPurchaseDto> getAllMaterialPurchases() {
        List<RawMaterialPurchase> materialPurchases = materialPurchaseRepository.findAll();
        return materialPurchases.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public RawMaterialPurchaseDto getMaterialPurchaseById(Long id) {
        var materialPurchase = materialPurchaseRepository.findById(id).get();
        return convertToDto(materialPurchase);
    }

    public Optional<RawMaterialPurchase> getMaterialPurchase(Long id) {
        return Optional.of(materialPurchaseRepository.findById(id).get());
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
    public int getNumberOfPurchasesByEmployeeAndMonth(Employee employee, int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        Date start = java.sql.Date.valueOf(startDate);
        Date end = java.sql.Date.valueOf(endDate);
        return materialPurchaseRepository.countByEmployeeAndDateBetween(employee, start, end);
    }
}
