package com.example.production.service;

import com.example.production.dto.EmployeeDto;
import com.example.production.model.Employee;
import com.example.production.model.Position;
import com.example.production.repository.EmployeeRepository;
import com.example.production.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final PositionService positionService;
    private final PasswordEncoder passwordEncoder;

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

    @Transactional
    public EmployeeDto saveEmployee(EmployeeDto employeeDto) {
        employeeDto.setPassword(passwordEncoder.encode(employeeDto.getPassword()));
        employeeDto.setEnabled(Boolean.TRUE);

        Employee employee = convertToEntity(employeeDto);
        employee = employeeRepository.save(employee);

        Long roleId = getRoleToNewUser(employee.getPosition().getName());
        roleRepository.createRole(employee.getId(), roleId);
        System.out.println("Role " + roleId + "  Employee: " + employee.getId());

        return convertToDto(employee);
    }

    public Long getRoleToNewUser(String position) {
        String role = null;
        switch (position) {
            case "Administrator":
                role = "ADMIN";
                break;
            case "Director":
                role ="DIRECTOR";
                break;
            case "Accountant":
                role ="ACCOUNTANT";
                break;
            case "Production manager":
            case "Sale manager":
                role ="MANAGER";
                break;
            case "Technologist":
                role = "TECHNOLOGIST";
                break;
        }
        return roleRepository.getIdByRole(role);
    }

//    public EmployeeDto updateEmployee(EmployeeDto employeeDto) {
//        boolean existingEmployee = employeeRepository.existsById(employeeDto.getId());
//        if (!existingEmployee) {
//            throw new NoSuchElementException("Employee with name " + employeeDto.getFullName() + " not found.");
//        }
//        employeeDto.setEnabled(true);
//
//        Employee employee = convertToEntity(employeeDto);
//        employee = employeeRepository.save(employee);
//
//        Long roleId = getRoleToNewUser(employee.getPosition().getName());
//        System.out.println("Role " + roleId + "  Employee: " + employee.getId());
//        roleRepository.updateRole(employee.getId(), roleId);
//
//        return convertToDto(employee);
//    }
public EmployeeDto updateEmployee(EmployeeDto employeeDto) {
    boolean existingEmployee = employeeRepository.existsById(employeeDto.getId());
    if (!existingEmployee) {
        throw new NoSuchElementException("Employee with name " + employeeDto.getFullName() + " not found.");
    }
    employeeDto.setEnabled(true);

    Employee employee = convertToEntity(employeeDto);
    employee = employeeRepository.save(employee);

    Long roleId = getRoleToNewUser(employee.getPosition().getName());
//    roleRepository.deleteRoleByEmployeeId(employee.getId());
    roleRepository.createRole(employee.getId(), roleId);

    return convertToDto(employee);
}

    public void deleteEmployee(Long id) {
        roleRepository.deleteRoleByEmployeeId(id);
        employeeRepository.deleteById(id);
    }

    public EmployeeDto convertToDto(Employee employee) {
        var position = positionService.getPositionById(employee.getPosition().getId());
        return EmployeeDto.builder()
                .id(employee.getId())
                .fullName(employee.getFullName())
                .position(position)
                .salary(employee.getSalary())
                .address(employee.getAddress())
                .phoneNumber(employee.getPhoneNumber())
                .email(employee.getEmail())
                .password(employee.getPassword())
                .enabled(employee.isEnabled())
                .build();
    }

    public Employee convertToEntity(EmployeeDto employeeDto) {
        Position position = null;
        if (employeeDto.getPosition() != null) {
            position = positionService.getPosition(employeeDto.getPosition().getId()).orElse(null);
        }
        return Employee.builder()
                .id(employeeDto.getId())
                .fullName(employeeDto.getFullName())
                .position(position)
                .salary(employeeDto.getSalary())
                .address(employeeDto.getAddress())
                .phoneNumber(employeeDto.getPhoneNumber())
                .email(employeeDto.getEmail())
                .password(employeeDto.getPassword())
                .enabled(employeeDto.isEnabled())
                .build();
    }

}
