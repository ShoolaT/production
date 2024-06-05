package com.example.production.service;

import com.example.production.model.Salary;
import com.example.production.repository.SalaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SalaryService {
    private final SalaryRepository salaryRepository;

    public Optional<Salary> getSalary(Long id) {
        return Optional.of(salaryRepository.findById(id).get());
    }
    @Transactional
    public List<Salary> getSalariesByYearAndMonth(int year, int month) {
        List<Salary> salaries = salaryRepository.findByYearAndMonth(year, month);

        return salaries;
    }
    @Transactional
    public List<Salary> getSalariesByDate(String startDate, String endDate) {

        int startYear = Integer.parseInt(startDate.substring(0, 4));
        int startMonth = Integer.parseInt(startDate.substring(5, 7));

        int endYear = Integer.parseInt(endDate.substring(0, 4));
        int endMonth = Integer.parseInt(endDate.substring(5, 7));
        System.out.println(startYear + " " + startMonth + "   "+ endYear + "  "+ endMonth);

        return salaryRepository.getSalariesByDate(startYear, startMonth, endYear, endMonth);
    }
    public void updateSalary(Long id, float general) {
        boolean existingSalary = salaryRepository.existsById(id);
        if (!existingSalary) {
            throw new NoSuchElementException("Salary with id " + id + " not found.");
        }
        salaryRepository.updateSalary(id, general);
    }
    public boolean updateSalariesIssuedStatus(int year, int month) {
        return salaryRepository.issueSalaries(year, month);
    }
    public float getTotalSalaryForMonth(int year, int month) {
        return salaryRepository.calculateTotalSalary(year, month);
    }
    public float calculateTotalGeneral(List<Salary> salaries) {
        float totalGeneral = 0;
        for (Salary salary : salaries) {
            totalGeneral += salary.getGeneral();
        }
        return totalGeneral;
    }


}
