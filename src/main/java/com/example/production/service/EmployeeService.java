package com.example.production.service;

import com.example.production.dto.EmployeeDto;
import com.example.production.model.Employee;
import com.example.production.model.Position;
import com.example.production.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final PositionService positionService;

    public Page<EmployeeDto> getEmployees(int page, int size, String sort) {
        var list = employeeRepository.findAll(PageRequest.of(page, size, Sort.by(sort)));
        return toPage(list.getContent(), PageRequest.of(list.getNumber(), list.getSize(), list.getSort()));
    }

    private Page<EmployeeDto> toPage(List<Employee> employees, Pageable pageable) {
        var list = employees.stream()
                .map(this::convertToDto)
                .toList();
        if (pageable.getOffset() >= list.size()) {
            return Page.empty();
        }
        int startIndex = (int) pageable.getOffset();
        int endIndex = (int) ((pageable.getOffset() + pageable.getPageSize()) > list.size() ?
                list.size() :
                pageable.getOffset() + pageable.getPageSize());
        List<EmployeeDto> subList = list.subList(startIndex, endIndex);
        return new PageImpl<>(subList, pageable, list.size());
    }
    public List<EmployeeDto> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public EmployeeDto getEmployeeById(Long id) {
        var employee = employeeRepository.findById(id).get();
        return convertToDto(employee);
    }
    public Optional<Employee> getEmployee(Long id) {
        return Optional.of(employeeRepository.findById(id).get());
    }

    public EmployeeDto saveEmployee(EmployeeDto employeeDto) {
        Employee employee = convertToEntity(employeeDto);
        employee = employeeRepository.save(employee);
        return convertToDto(employee);
    }
    public EmployeeDto updateEmployee(EmployeeDto employeeDto) {
        boolean existingEmployee = employeeRepository.existsById(employeeDto.getId());
        if(!existingEmployee){
            throw new NoSuchElementException("Employee with name " + employeeDto.getFullName() + " not found.");
        }
        Employee employee = convertToEntity(employeeDto);
        employee = employeeRepository.save(employee);
        return convertToDto(employee);
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    public EmployeeDto convertToDto(Employee employee) {
        var position = positionService.getPositionById(employee.getPosition().getId());
        return EmployeeDto.builder()
                .id(employee.getId())
                .fullName(employee.getFullName())
                .position(position)
                .address(employee.getAddress())
                .salary(employee.getSalary())
                .phoneNumber(employee.getPhoneNumber())
                .build();
    }

    private Employee convertToEntity(EmployeeDto employeeDto) {
        Position position = null;
        if (employeeDto.getPosition() != null) {
            position = positionService.getPosition(employeeDto.getPosition().getId()).orElse(null);
        }
        return Employee.builder()
                .id(employeeDto.getId())
                .fullName(employeeDto.getFullName())
                .position(position)
                .address(employeeDto.getAddress())
                .salary(employeeDto.getSalary())
                .phoneNumber(employeeDto.getPhoneNumber())
                .build();
    }

}
