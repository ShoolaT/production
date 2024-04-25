package com.example.production.service;

import com.example.production.dto.PaymentHistoryDto;
import com.example.production.model.PaymentHistory;
import com.example.production.repository.PaymentHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentHistoryService {
    private final PaymentHistoryRepository paymentHistoryRepository;
    private final BankService bankService;
    @Transactional
    public PaymentHistoryDto getPaymentHistory(Long id, Date date) {
        PaymentHistory paymentHistory = paymentHistoryRepository.getPaymentHistory(id, date);
        return convertToDto(paymentHistory);
    }
    @Transactional
    public List<PaymentHistoryDto> getPaymentHistories(Long bankId) {
        System.out.println("BAAANNN: "+ bankId);
        List<PaymentHistory> paymentHistories = paymentHistoryRepository.findAllByBankId(bankId);
        return paymentHistories.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    public float getPaymentTotalAmount(Long id) {
        return paymentHistoryRepository.getPaymentTotalAmount(id);
    }
//    public boolean existsByBankId(Long id){
//        return paymentHistoryRepository.existsPaymentHistoryByBankId(id);
//    }

    public PaymentHistoryDto convertToDto(PaymentHistory paymentHistory) {
        var bank = bankService.getBankById(paymentHistory.getBank().getId());
        return PaymentHistoryDto.builder()
                .id(paymentHistory.getId())
                .paymentDate(paymentHistory.getPaymentDate())
                .baseAmount(paymentHistory.getBaseAmount())
                .percentAmount(paymentHistory.getPercentAmount())
                .allAmount(paymentHistory.getAllAmount())
                .overdue(paymentHistory.getOverdue())
                .fineAmount(paymentHistory.getFineAmount())
                .totalAmount(paymentHistory.getTotalAmount())
                .residue(paymentHistory.getResidue())
                .bank(bank)
                .build();
    }
    public PaymentHistory convertToEntity(PaymentHistoryDto paymentHistoryDto) {
        var bank = bankService.getBank(paymentHistoryDto.getBank().getId()).get();
        return PaymentHistory.builder()
                .id(paymentHistoryDto.getId())
                .paymentDate(paymentHistoryDto.getPaymentDate())
                .baseAmount(paymentHistoryDto.getBaseAmount())
                .percentAmount(paymentHistoryDto.getPercentAmount())
                .allAmount(paymentHistoryDto.getAllAmount())
                .overdue(paymentHistoryDto.getOverdue())
                .fineAmount(paymentHistoryDto.getFineAmount())
                .totalAmount(paymentHistoryDto.getTotalAmount())
                .residue(paymentHistoryDto.getResidue())
                .bank(bank)
                .build();
    }

    public Long savePaymentHistory(PaymentHistoryDto paymentHistory) {
        return paymentHistoryRepository.savePaymentHistory(
                paymentHistory.getPaymentDate(),
                paymentHistory.getBaseAmount(),
                paymentHistory.getPercentAmount(),
                paymentHistory.getAllAmount(),
                paymentHistory.getOverdue(),
                paymentHistory.getFineAmount(),
                paymentHistory.getTotalAmount(),
                paymentHistory.getResidue(),
                paymentHistory.getBank().getId()
        );
    }
}
