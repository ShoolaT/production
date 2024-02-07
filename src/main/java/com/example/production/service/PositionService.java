package com.example.production.service;

import com.example.production.dto.PositionDto;
import com.example.production.model.Position;
import com.example.production.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PositionService {
    private final PositionRepository positionRepository;

    public Page<PositionDto> getPositions(int page, int size, String sort) {
        var list = positionRepository.findAll(PageRequest.of(page, size, Sort.by(sort)));
        return toPage(list.getContent(), PageRequest.of(list.getNumber(), list.getSize(), list.getSort()));
    }

    private Page<PositionDto> toPage(List<Position> positions, Pageable pageable) {
        var list = positions.stream()
                .map(this::convertToDto)
                .toList();
        if (pageable.getOffset() >= list.size()) {
            return Page.empty();
        }
        int startIndex = (int) pageable.getOffset();
        int endIndex = (int) ((pageable.getOffset() + pageable.getPageSize()) > list.size() ?
                list.size() :
                pageable.getOffset() + pageable.getPageSize());
        List<PositionDto> subList = list.subList(startIndex, endIndex);
        return new PageImpl<>(subList, pageable, list.size());
    }
    public List<PositionDto> getAllPositions() {
        List<Position> positions = positionRepository.findAll();
        return positions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public PositionDto getPositionById(Long id) {
        var position = positionRepository.findById(id).get();
        return convertToDto(position);
    }
    public Optional<Position> getPosition(Long id) {
        return Optional.of(positionRepository.findById(id).get());
    }

    public PositionDto savePosition(PositionDto positionDto) {
        Position position = convertToEntity(positionDto);
        position = positionRepository.save(position);
        return convertToDto(position);
    }

    public void deletePosition(Long id) {
        positionRepository.deleteById(id);
    }

    public PositionDto convertToDto(Position position) {
        return PositionDto.builder()
                .id(position.getId())
                .name(position.getName())
                .build();
    }

    private Position convertToEntity(PositionDto positionDto) {
        return Position.builder()
                .id(positionDto.getId())
                .name(positionDto.getName())
                .build();
    }
}
