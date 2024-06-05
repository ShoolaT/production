package com.example.production.controller;

import com.example.production.dto.BankDto;
import com.example.production.dto.PaymentHistoryDto;
import com.example.production.model.Bank;
import com.example.production.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayInputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/credits")
@RequiredArgsConstructor
@Slf4j
public class BankController {
    private final BankService bankService;
    private final PaymentHistoryService paymentHistoryService;
    private final BudgetService budgetService;
    private final EmployeeService employeeService;
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
        List<Bank> banks = bankService.getBanks(null, null);
        model.addAttribute("banks", banks);
        model.addAttribute("budget", formatter(budgetService.getBudget()));

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        model.addAttribute("roles", roles);
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
    @GetMapping("/all/report")
    public String getBanksToReport(Model model,
                                         @RequestParam(name = "startDate", required = false) String startDate,
                                         @RequestParam(name = "endDate", required = false) String endDate) {
        List<Bank> banks = null;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        if (startDate != null && endDate != null) {
            try {
                Date start = formatter.parse(startDate);
                Date end = formatter.parse(endDate);
                banks = bankService.getBanks(start, end);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            model.addAttribute("startD", startDate);
            model.addAttribute("endD", endDate);
            model.addAttribute("total", bankService.calculateTotalSum(banks));
        }

        model.addAttribute("banks", banks);

        return "report/banks";
    }


    @GetMapping("/all/print")
    public ResponseEntity<byte[]> printReport(@RequestParam(name = "startDate", required = false) String startDate,
                                              @RequestParam(name = "endDate", required = false) String endDate) {
        List<Bank> banks;
        Date start = null, end = null;
        if (startDate == null || endDate == null) {
            banks = null;
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            try {
                start = formatter.parse(startDate);
                end = formatter.parse(endDate);
                banks = bankService.getBanks(start, end);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;

        String user = employeeService.getEmployeeDetailsByEmail(userDetails.getUsername());

        String[] detailsArray = user.split(",");

        Float total= null;
        if(banks != null) {
            total = (float) bankService.calculateTotalSum(banks);
        }
        ByteArrayInputStream bis = ExportPdf.banksReport(banks, detailsArray, start, end, total);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "bank_report.pdf");

        byte[] bytes = new byte[0];
        bytes = new byte[bis.available()];
        bis.read(bytes, 0, bis.available());

        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

}
