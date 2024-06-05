package com.example.production.service;

import com.example.production.dto.BankDto;
import com.example.production.model.Bank;
import com.example.production.model.Salary;
import com.example.production.repository.BankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BankService {
    private final BankRepository bankRepository;
    @Transactional
    public List<Bank> getBanks(Date startDate, Date endDate) {
        if (startDate == null && endDate == null) {
            return bankRepository.getBanksByDate(null, null);
        } else {
            return bankRepository.getBanksByDate(startDate, endDate);
        }
    }
    public Optional<Bank> getBank(Long id) {
        return Optional.of(bankRepository.findById(id).get());
    }
    public BankDto getBankById(Long id) {
        var bank = bankRepository.findById(id).get();
        return convertToDto(bank);
    }
    public boolean isPaid(Long id){
        var bank = bankRepository.findById(id).get();
        return bank.isPaid();
    }
    public float calculateTotalSum(List<Bank> banks) {
        float totalSum = 0;
        for (Bank bank : banks) {
            totalSum += bank.getSum();
        }
        return totalSum;
    }

    public BankDto saveBank(BankDto bankDto) {
        Long bankId = bankRepository.createBank(
                bankDto.getSum(),
                bankDto.getMonth(),
                bankDto.getPercent(),
                bankDto.getFine(),
                bankDto.getReceiptDate(),
                bankDto.isPaid()
        );
        bankDto.setId(bankId);

        return bankDto;
    }

    public BankDto convertToDto(Bank bank) {
        return BankDto.builder()
                .id(bank.getId())
                .sum(bank.getSum())
                .month(bank.getMonth())
                .percent(bank.getPercent())
                .fine(bank.getFine())
                .receiptDate(bank.getReceiptDate())
                .isPaid(bank.isPaid())
                .build();
    }
}
