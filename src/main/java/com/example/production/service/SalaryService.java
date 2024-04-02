package com.example.production.service;

import com.example.production.dto.EmployeeDto;
import com.example.production.model.Budget;
import com.example.production.model.Employee;
import com.example.production.model.Salary;
import com.example.production.repository.SalaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SalaryService {
    private final SalaryRepository salaryRepository;
    private final ProductProductionService productProductionService;
    private final ProductSaleService productSaleService;
    private final RawMaterialPurchaseService purchaseService;
    private final EmployeeService employeeService;
    private final BudgetService budgetService;

    public Optional<Salary> getSalary(Long id) {
        return Optional.of(salaryRepository.findById(id).get());
    }
    public List<Salary> getSalariesByYearAndMonth(int year, int month) {
        List<Salary> salaries = salaryRepository.findByYearAndMonth(year, month);

        if (salaries.isEmpty()) {
            salaries = generateSalaries(year, month);
        }
        return salaries;
    }

    private List<Salary> generateSalaries(int year, int month) {
        List<EmployeeDto> employees = employeeService.getAllEmployees();
        Budget budget = budgetService.budgetGet();

        for (EmployeeDto employee : employees) {
            Employee employee1 = employeeService.convertToEntity(employee);

            int numberOfPurchase = purchaseService.getNumberOfPurchasesByEmployeeAndMonth(employee1, year, month);
            int numberOfProduction = productProductionService.getNumberOfProductionsByEmployeeAndMonth(employee1, year, month);
            int numberOfSale = productSaleService.getNumberOfSalesByEmployeeAndMonth(employee1, year, month);
            int common = numberOfPurchase + numberOfProduction + numberOfSale;

            float bonus = budget.getBonus() * common * employee.getSalary() / 100;
            float general = employee.getSalary() + bonus;

            Salary salary = Salary.builder()
                    .year(year)
                    .month(month)
                    .employee(employee1)
                    .numberOfPurchase(numberOfPurchase)
                    .numberOfProduction(numberOfProduction)
                    .numberOfSale(numberOfSale)
                    .common(common)
                    .salary(employee.getSalary())
                    .bonus(bonus)
                    .general(general)
                    .issued(false)
                    .build();
            salaryRepository.save(salary);
        }
        return salaryRepository.findByYearAndMonth(year, month);
    }
    public Salary updateSalary(Salary salary) {
        boolean existingSalary = salaryRepository.existsById(salary.getId());
        if (!existingSalary) {
            throw new NoSuchElementException("Salary with id " + salary.getId() + " not found.");
        }
        salary.setEmployee(employeeService.getEmployee(salary.getEmployee().getId()).get());
        return salaryRepository.save(salary);
    }
    public void updateSalariesIssuedStatus(int year, int month, boolean issued) {
        List<Salary> salaries = salaryRepository.findByYearAndMonth(year, month);
        for (Salary salary : salaries) {
            salary.setIssued(issued);
        }
        salaryRepository.saveAll(salaries);
    }



}
