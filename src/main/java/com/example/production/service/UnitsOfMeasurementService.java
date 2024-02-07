package com.example.production.service;

import com.example.production.dto.UnitOfMeasurementDto;
import com.example.production.model.UnitsOfMeasurement;
import com.example.production.repository.UnitOfMeasurementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UnitsOfMeasurementService {
    private final UnitOfMeasurementRepository unitOfMeasurementRepository;

    public Page<UnitOfMeasurementDto> getMeasurements(int page, int size, String sort) {
        var list = unitOfMeasurementRepository.findAll(PageRequest.of(page, size, Sort.by(sort)));
        return toPage(list.getContent(), PageRequest.of(list.getNumber(), list.getSize(), list.getSort()));
    }

    private Page<UnitOfMeasurementDto> toPage(List<UnitsOfMeasurement> measurements, Pageable pageable) {
        var list = measurements.stream()
                .map(this::convertToDto)
                .toList();
        if (pageable.getOffset() >= list.size()) {
            return Page.empty();
        }
        int startIndex = (int) pageable.getOffset();
        int endIndex = (int) ((pageable.getOffset() + pageable.getPageSize()) > list.size() ?
                list.size() :
                pageable.getOffset() + pageable.getPageSize());
        List<UnitOfMeasurementDto> subList = list.subList(startIndex, endIndex);
        return new PageImpl<>(subList, pageable, list.size());
    }
    public List<UnitOfMeasurementDto> getAllMeasurements() {
        List<UnitsOfMeasurement> measurements = unitOfMeasurementRepository.findAll();
        return measurements.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public UnitOfMeasurementDto getMeasurementById(Long id) {
        var measurement = unitOfMeasurementRepository.findById(id).get();
        return convertToDto(measurement);
    }
    public Optional<UnitsOfMeasurement> getMeasurement(Long id) {
        return Optional.of(unitOfMeasurementRepository.findById(id).get());
    }

    public UnitOfMeasurementDto saveMeasurement(UnitOfMeasurementDto measurementDto) {
        UnitsOfMeasurement unitsOfMeasurement = convertToEntity(measurementDto);
        unitsOfMeasurement = unitOfMeasurementRepository.save(unitsOfMeasurement);
        return convertToDto(unitsOfMeasurement);
    }

    public void deleteMeasurement(Long id) {
        unitOfMeasurementRepository.deleteById(id);
    }



    public UnitOfMeasurementDto convertToDto(UnitsOfMeasurement measurement) {
        return UnitOfMeasurementDto.builder()
                .id(measurement.getId())
                .name(measurement.getName())
                .build();
    }

    private UnitsOfMeasurement convertToEntity(UnitOfMeasurementDto measurementDto) {
        return UnitsOfMeasurement.builder()
                .id(measurementDto.getId())
                .name(measurementDto.getName())
                .build();
    }
}
