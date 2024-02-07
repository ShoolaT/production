package com.example.production.service;

import com.example.production.dto.RawMaterialDto;
import com.example.production.model.RawMaterial;
import com.example.production.model.UnitsOfMeasurement;
import com.example.production.repository.RawMaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RawMaterialService {
    private final RawMaterialRepository rawMaterialRepository;
    private final UnitsOfMeasurementService unitsOfMeasurementService;

    public Page<RawMaterialDto> getMaterials(int page, int size, String sort) {
        var list = rawMaterialRepository.findAll(PageRequest.of(page, size, Sort.by(sort)));
        return toPage(list.getContent(), PageRequest.of(list.getNumber(), list.getSize(), list.getSort()));
    }

    private Page<RawMaterialDto> toPage(List<RawMaterial> materials, Pageable pageable) {
        var list = materials.stream()
                .map(this::convertToDto)
                .toList();
        if (pageable.getOffset() >= list.size()) {
            return Page.empty();
        }
        int startIndex = (int) pageable.getOffset();
        int endIndex = (int) ((pageable.getOffset() + pageable.getPageSize()) > list.size() ?
                list.size() :
                pageable.getOffset() + pageable.getPageSize());
        List<RawMaterialDto> subList = list.subList(startIndex, endIndex);
        return new PageImpl<>(subList, pageable, list.size());
    }
    public List<RawMaterialDto> getAllMaterials() {
        List<RawMaterial> materials = rawMaterialRepository.findAll();
        return materials.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public RawMaterialDto getMaterialById(Long id) {
        var rawMaterial = rawMaterialRepository.findById(id).get();
        return convertToDto(rawMaterial);
    }
    public Optional<RawMaterial> getMaterial(Long id) {
        return Optional.of(rawMaterialRepository.findById(id).get());
    }

    public RawMaterialDto saveMaterial(RawMaterialDto materialDto) {
        RawMaterial rawMaterial = convertToEntity(materialDto);
        rawMaterial = rawMaterialRepository.save(rawMaterial);
        return convertToDto(rawMaterial);
    }

    public void deleteMaterial(Long id) {
        rawMaterialRepository.deleteById(id);
    }

    public RawMaterialDto convertToDto(RawMaterial rawMaterial) {
        var measurement = unitsOfMeasurementService.getMeasurementById(rawMaterial.getUnitsOfMeasurement().getId());
        return RawMaterialDto.builder()
                .id(rawMaterial.getId())
                .name(rawMaterial.getName())
                .unitsOfMeasurement(measurement)
                .quantity(rawMaterial.getQuantity())
                .amount(rawMaterial.getAmount())
                .build();
    }

    private RawMaterial convertToEntity(RawMaterialDto rawMaterialDto) {
        UnitsOfMeasurement measurement = unitsOfMeasurementService.getMeasurement(rawMaterialDto.getUnitsOfMeasurement().getId()).get();
        return RawMaterial.builder()
                .id(rawMaterialDto.getId())
                .name(rawMaterialDto.getName())
                .unitsOfMeasurement(measurement)
                .quantity(rawMaterialDto.getQuantity())
                .amount(rawMaterialDto.getAmount())
                .build();
    }

}