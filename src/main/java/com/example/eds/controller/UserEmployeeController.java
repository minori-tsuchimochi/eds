package com.example.eds.controller;

import com.example.eds.entity.Employee;
import com.example.eds.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class UserEmployeeController {
    private final EmployeeRepository employeeRepository;

    @GetMapping("/user/employees")
    public String list(
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Long positionId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            Model model
    ) {
        Page<Employee> employees = employeeRepository.search(
                departmentId,
                positionId,
                keyword,
                PageRequest.of(page, 5)
        );
        model.addAttribute("employees", employees);
        model.addAttribute("departmentId", departmentId);
        model.addAttribute("positionId", positionId);
        model.addAttribute("keyword", keyword);
        return "user/employees/list";
    }
}
