package com.example.eds.controller.admin;

import com.example.eds.entity.*;
import com.example.eds.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin/employees")
@RequiredArgsConstructor
public class AdminEmployeeController {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;

    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("positions", positionRepository.findAll());
        return "admin/employee/register";
    }

    @PostMapping("/register")
    public String registerEmployee(
            @RequestParam String employeeNumber,
            @RequestParam String name,
            @RequestParam Long departmentId,
            @RequestParam Long positionId,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String joinDate
    ) {
        Employee emp = new Employee();
        emp.setEmployeeNumber(employeeNumber);
        emp.setName(name);
        emp.setDepartment(departmentRepository.findById(departmentId).orElseThrow());
        emp.setPosition(positionRepository.findById(positionId).orElseThrow());
        emp.setEmail(email);
        emp.setPhone(phone);
        emp.setJoinDate(LocalDate.parse(joinDate));

        employeeRepository.save(emp);

        return "redirect:/admin/employees/register";
    }
}
