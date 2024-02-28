package com.example.production.controller;

import com.example.production.dto.EmployeeDto;
import com.example.production.dto.PositionDto;
import com.example.production.service.EmployeeService;
import com.example.production.service.PositionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/employees")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {
    private final EmployeeService employeeService;
    private final PositionService positionService;
    @GetMapping("/create")
    public String createShowEmployee(Model model) {
        List<PositionDto> positions = positionService.getAllPositions();
        model.addAttribute("positions", positions);
        return "employees/createEmployee";
    }
    @PostMapping("/create")
    public String createEmployee(@ModelAttribute EmployeeDto employeeDto) {
        employeeService.saveEmployee(employeeDto);
        return "redirect:/employees/all";
    }

    @GetMapping("/all")
    public String getAllEmployees(Model model,
                                  @RequestParam(name = "sort", defaultValue = "id") String sortCriteria) {
        var employees = employeeService.getEmployees(0,9,sortCriteria);
        model.addAttribute("employees", employees);
        model.addAttribute("positions", positionService.getAllPositions());
        return "employees/allEmployees";
    }
    @GetMapping("/{id}")
    public String getEmployeeById(@PathVariable Long id, Model model) {
        EmployeeDto employee = employeeService.getEmployeeById(id);
        model.addAttribute("employee", employee);
        model.addAttribute("company",employee.getPosition());
        return "employees/getEmployee";
    }
    @PostMapping("/{id}/edit")
    public String updateEmployee(@PathVariable Long id, @ModelAttribute EmployeeDto employeeDto) {
        employeeDto.setId(id);
        employeeService.updateEmployee(employeeDto);
        return "redirect:/employees/all";
    }
    @GetMapping("/{id}/edit")
    public String updateShowEmployee(@PathVariable Long id, Model model) {
        EmployeeDto employeeDto = employeeService.getEmployeeById(id);
        if (employeeDto != null) {
            model.addAttribute("employeeDto", employeeDto);
            model.addAttribute("positions",positionService.getAllPositions());
        } else {
            new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return "employees/updateEmployee";
    }

    @GetMapping("/{id}/delete/confirmed")
    public String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return "redirect:/employees/all";
    }
}
