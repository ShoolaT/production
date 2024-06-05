package com.example.production.controller;

import com.example.production.model.Salary;
import com.example.production.service.BudgetService;
import com.example.production.service.EmployeeService;
import com.example.production.service.ExportPdf;
import com.example.production.service.SalaryService;
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
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/salaries")
@RequiredArgsConstructor
@Slf4j
public class SalaryController {
    private final SalaryService salaryService;
    private final BudgetService budgetService;
    private final EmployeeService employeeService;

    @GetMapping("/all")
    public String showSalariesPage(Model model, Integer year, Integer month) {
        YearMonth currentYearMonth = YearMonth.now();
        int currentYear = currentYearMonth.getYear();
        int currentMonth = currentYearMonth.getMonthValue();

        if (year == null || month == null) {
            year = currentYear;
            month = currentMonth;
        }

        List<Salary> salaries = salaryService.getSalariesByYearAndMonth(year, month);

        float totalAmount = salaryService.getTotalSalaryForMonth(year,month);

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#0.00", symbols);

        model.addAttribute("totalAmount", df.format(totalAmount));

        model.addAttribute("budget", df.format(budgetService.getBudget()));
        model.addAttribute("salaries", salaries);
        model.addAttribute("years", getYears());
        model.addAttribute("months", getMonths());

        model.addAttribute("currentYear", String.valueOf(year));
        model.addAttribute("currentMonth", month);

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        model.addAttribute("roles", roles);

        return "salaries/allSalaries";
    }

    @PostMapping("/all")
    public String showSalariesByYearAndMonth(Model model, @ModelAttribute("year") Integer year, @ModelAttribute("month") Integer month) {
        YearMonth currentYearMonth = YearMonth.now();
        int currentYear = currentYearMonth.getYear();
        int currentMonth = currentYearMonth.getMonthValue();

        if (year == null || month == null) {
            year = currentYear;
            month = currentMonth;
            model.addAttribute("currentYear", String.valueOf(currentYear));
            model.addAttribute("currentMonth", currentMonth);
        }else {
            model.addAttribute("currentYear", String.valueOf(year));
            model.addAttribute("currentMonth", month);
        }

        List<Salary> salaries = salaryService.getSalariesByYearAndMonth(year, month);

        float totalAmount = salaryService.getTotalSalaryForMonth(year, month);

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#0.00", symbols);

        model.addAttribute("totalAmount", df.format(totalAmount));

        model.addAttribute("budget", df.format(budgetService.getBudget()));
        model.addAttribute("salaries", salaries);
        model.addAttribute("years", getYears());
        model.addAttribute("months", getMonths());

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        model.addAttribute("roles", roles);

        return "salaries/allSalaries";
    }
    @GetMapping("/{id}/edit")
    public String editSalary(@PathVariable Long id, Model model) {
        Optional<Salary> salary = salaryService.getSalary(id);
        if (salary.isPresent()) {
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
            symbols.setDecimalSeparator('.');
            DecimalFormat df = new DecimalFormat("#0.00", symbols);

            String formattedSalary = df.format(salary.get().getSalary());
            model.addAttribute("salaryAmount", formattedSalary);

            String formattedBonus = df.format(salary.get().getBonus());
            model.addAttribute("bonus", formattedBonus);

            String formattedGeneral = df.format(salary.get().getGeneral());

            model.addAttribute("year", String.valueOf(salary.get().getYear()));
            model.addAttribute("general", formattedGeneral);
            model.addAttribute("salary1", salary.get());
        } else {
            new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return "salaries/updateSalaries";
    }

    @PostMapping("/{id}/edit")
    public String updateSalary(@PathVariable Long id,
                               @ModelAttribute Salary salary,
                               RedirectAttributes redirectAttributes) {
        salaryService.updateSalary(id, salary.getGeneral());

        int year = salary.getYear();
        int month = salary.getMonth();

        redirectAttributes.addAttribute("year", String.valueOf(year));
        redirectAttributes.addAttribute("month", month);

        return "redirect:/salaries/all";
    }
    @PostMapping("/issue")
    public String issueSalaries(Model model, @RequestParam("year") int year, @RequestParam("month") int month,
                                RedirectAttributes redirectAttributes) {
        if (salaryService.updateSalariesIssuedStatus(year, month)) {

            redirectAttributes.addAttribute("year", String.valueOf(year));
            redirectAttributes.addAttribute("month", month);
            return "redirect:/salaries/all";
        } else {
            model.addAttribute("errorMessage", "Insufficient budget to issue salaries");
            return "errorPage";
        }
    }
    @GetMapping("/all/report")
    public String getSalariesToReport(Model model,
                                      @RequestParam(name = "startDate", required = false) String startDate,
                                      @RequestParam(name = "endDate", required = false) String endDate) {
        List<Salary> salaries = null;

        if (startDate != null && endDate != null) {
            salaries = salaryService.getSalariesByDate(startDate, endDate);

            model.addAttribute("startD", startDate);
            model.addAttribute("endD", endDate);
            model.addAttribute("total", salaryService.calculateTotalGeneral(salaries));
        }

        model.addAttribute("salaries", salaries);

        return "report/salaries";
    }


    @GetMapping("/all/print")
    public ResponseEntity<byte[]> printReport(@RequestParam(name = "startDate", required = false) String startDate,
                                              @RequestParam(name = "endDate", required = false) String endDate) {
        List<Salary> salaries;
        Date start = null, end = null;
        if (startDate == null || endDate == null) {
            salaries = null;
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");

            try {
                start = formatter.parse(startDate);
                end = formatter.parse(endDate);

                salaries = salaryService.getSalariesByDate(startDate, endDate);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;

        String user = employeeService.getEmployeeDetailsByEmail(userDetails.getUsername());

        String[] details= user.split(",");
        Float total= null;
        if(salaries != null) {
             total = (float) salaryService.calculateTotalGeneral(salaries);
        }

        ByteArrayInputStream bis = ExportPdf.salariesReport(salaries, details, start, end, total);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "salary_report.pdf");

        byte[] bytes = new byte[0];
        bytes = new byte[bis.available()];
        bis.read(bytes, 0, bis.available());

        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    private static List<String> getYears() {
        int startYear = 2020;
        return IntStream.rangeClosed(startYear, 2030)
                .mapToObj(Integer::toString)
                .collect(Collectors.toList());
    }

    private List<String> getMonths() {
        return Arrays.stream(Month.values())
                .map(month -> month.getDisplayName(TextStyle.FULL, Locale.ENGLISH))
                .collect(Collectors.toList());
    }
}
