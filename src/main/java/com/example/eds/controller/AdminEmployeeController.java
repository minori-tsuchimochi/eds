package com.example.eds.controller.admin;

import com.example.eds.entity.Employee;
import com.example.eds.repository.DepartmentRepository;
import com.example.eds.repository.EmployeeRepository;
import com.example.eds.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    @GetMapping
    public String list(
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Long positionId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            Model model) {
        int size = 5;
        PageRequest pageable = PageRequest.of(page, size);
        Page<Employee> employeePage = employeeRepository.search(departmentId, positionId, keyword, pageable);

        model.addAttribute("employees", employeePage.getContent());
        model.addAttribute("employeePage", employeePage);
        model.addAttribute("currentPage", page);
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("positions", positionRepository.findAll());
        model.addAttribute("departmentId", departmentId);
        model.addAttribute("positionId", positionId);
        model.addAttribute("keyword", keyword);
        return "admin/employees/list";
    }

    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("positions", positionRepository.findAll());
        return "admin/employees/register";
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

        return "redirect:/admin/employees";
    }

    @GetMapping("/{id}/edit")
    public String showEdit(@PathVariable Long id, Model model) {
        Employee emp = employeeRepository.findById(id).orElseThrow();
        model.addAttribute("employee", emp);
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("positions", positionRepository.findAll());
        return "admin/employees/edit";
    }

    @PostMapping("/{id}/edit")
    public String update(
            @PathVariable Long id,
            @RequestParam String employeeNumber,
            @RequestParam String name,
            @RequestParam Long departmentId,
            @RequestParam Long positionId,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String joinDate
    ) {
        Employee emp = employeeRepository.findById(id).orElseThrow();
        emp.setEmployeeNumber(employeeNumber);
        emp.setName(name);
        emp.setDepartment(departmentRepository.findById(departmentId).orElseThrow());
        emp.setPosition(positionRepository.findById(positionId).orElseThrow());
        emp.setEmail(email);
        emp.setPhone(phone);
        if (joinDate != null && !joinDate.isBlank()) {
            emp.setJoinDate(LocalDate.parse(joinDate));
        }
        employeeRepository.save(emp);

        return "redirect:/admin/employees";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        employeeRepository.deleteById(id);
        return "redirect:/admin/employees";
    }
}
