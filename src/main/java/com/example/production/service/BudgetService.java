package com.example.production.service;

import com.example.production.model.Budget;
import com.example.production.repository.BudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;

    @Transactional
    public boolean checkBudget(float requiredAmount) {
        Budget budget = budgetRepository.findById(1L).orElseThrow(() -> new NoSuchElementException("Budget not found"));

        return budget.getAmount() >= requiredAmount;
    }
    @Transactional
    public void decreaseBudget(float amount) {
        Budget budget = budgetRepository.findById(1L).orElseThrow(() -> new NoSuchElementException("Budget not found"));
        budget.setAmount(budget.getAmount() - amount);
        budgetRepository.save(budget);
    }

    @Transactional
    public void increaseBudget(float amount) {
        Budget budget = budgetRepository.findById(1L).orElseThrow(() -> new NoSuchElementException("Budget not found"));
        budget.setAmount(budget.getAmount() + amount);
        budgetRepository.save(budget);
    }

    public float getBudget(){
        Budget budget = budgetRepository.findById(1L).orElseThrow(() -> new NoSuchElementException("Budget not found"));
        return budget.getAmount();
    }
    public float getPercent(){
        Budget budget = budgetRepository.findById(1L).orElseThrow(() -> new NoSuchElementException("Budget not found"));
        return budget.getPercent();
    }
}
