package com.example.production.controller;

import com.example.production.dto.BankDto;
import com.example.production.dto.PaymentHistoryDto;
import com.example.production.service.BankService;
import com.example.production.service.BudgetService;
import com.example.production.service.PaymentHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/credits")
@RequiredArgsConstructor
@Slf4j
public class BankController {
    private final BankService bankService;
    private final PaymentHistoryService paymentHistoryService;
    private final BudgetService budgetService;

    @GetMapping("/create")
    public String createBank() {
        return "banks/createBank";
    }

    @PostMapping("/create")
    public String createBank(@ModelAttribute BankDto bankDto) {
        bankService.saveBank(bankDto);
        return "redirect:/credits/all";
    }

    @GetMapping("/all")
    public String getAllBanks(Model model) {
        List<BankDto> banks = bankService.getBanks();
        model.addAttribute("banks", banks);
        model.addAttribute("budget", formatter(budgetService.getBudget()));

        return "banks/allBanks";
    }

    @GetMapping("/pay/{id}")
    public String paymentHistoryForPay(@PathVariable Long id,
                                       @RequestParam(value = "payment_date", required = false) String dateString,
                                       @RequestParam(value = "errorQuantity", required = false) String errorQuantity,
                                       Model model) {
        Date date = null;
        if (dateString != null) {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                date = formatter.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (date == null) {
            date = new Date();
        }
        PaymentHistoryDto paymentHistoryDto = paymentHistoryService.getPaymentHistory(id, date);
        model.addAttribute("paymentHistory", paymentHistoryDto);
        model.addAttribute("baseAmount", formatter(paymentHistoryDto.getBaseAmount()));
        model.addAttribute("percentAmount", formatter(paymentHistoryDto.getPercentAmount()));
        model.addAttribute("allAmount", formatter(paymentHistoryDto.getAllAmount()));
        model.addAttribute("fineAmount", formatter(paymentHistoryDto.getFineAmount()));
        model.addAttribute("totalAmount", formatter(paymentHistoryDto.getTotalAmount()));
        model.addAttribute("residue", formatter(paymentHistoryDto.getResidue()));
        if (errorQuantity != null) {
            model.addAttribute("error", errorQuantity);
        }else {
            model.addAttribute("error", "");
        }

        return "banks/paymentHistoryForPay";
    }

    @PostMapping("/pay/{id}")
    public String paymentHistoryPay(@ModelAttribute PaymentHistoryDto paymentHistoryDto, @RequestParam("bankId") Long bankId, RedirectAttributes redirectAttributes) {
        BankDto bank = new BankDto();
        bank.setId(bankId);
        paymentHistoryDto.setBank(bank);

        Long id = paymentHistoryService.savePaymentHistory(paymentHistoryDto);
        if (id > 0) {
            redirectAttributes.addAttribute("id", paymentHistoryDto.getBank().getId());
            return "redirect:/credits/{id}/history";
        } else {
            redirectAttributes.addAttribute("id", paymentHistoryDto.getBank().getId());
            redirectAttributes.addAttribute("errorQuantity", "There is no such money.");
            return "redirect:/credits/pay/{id}";
        }
    }

    @GetMapping("/{id}/history")
    public String getPaymentHistory(@PathVariable Long id, Model model) {
        List<PaymentHistoryDto> paymentHistories = paymentHistoryService.getPaymentHistories(id);
        model.addAttribute("paymentHistories", paymentHistories);

        if (bankService.isPaid(id)) {
            model.addAttribute("totalAmount", formatter(paymentHistoryService.getPaymentTotalAmount(id)));
        }

        return "banks/getPaymentHistories";
    }

    private String formatter(float num) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#0.00", symbols);

        return df.format(num);
    }

}
