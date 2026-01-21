package com.example.eds.controller.admin;

import com.example.eds.entity.Employee;
import com.example.eds.repository.DepartmentRepository;
import com.example.eds.repository.EmployeeRepository;
import com.example.eds.repository.PositionRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam(defaultValue = "0") int page, Model model
    ) {
        PageRequest pageable = PageRequest.of(page, 5);
        Page<Employee> employeePage =
                employeeRepository.search(departmentId, positionId, keyword, pageable);

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
        model.addAttribute("employee", new Employee());
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("positions", positionRepository.findAll());
        return "admin/employees/register";
    }

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute Employee employee,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", departmentRepository.findAll());
            model.addAttribute("positions", positionRepository.findAll());
            return "admin/employees/register";
        }
        employee.setDepartment(
                departmentRepository.findById(employee.getDepartment().getId())
                        .orElseThrow(() -> new IllegalArgumentException("部署が選択されてません。"))
        );
        employee.setPosition(
                positionRepository.findById(employee.getPosition().getId())
                        .orElseThrow(() -> new IllegalArgumentException("役職が選択されてません。"))
        );
        employeeRepository.save(employee);
        return "redirect:/admin/employees";
    }

    @GetMapping("/{id}/edit")
    public String showEdit(@PathVariable Long id, Model model) {
        Employee emp = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("社員が存在しません。"));

        model.addAttribute("employee", emp);
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("positions", positionRepository.findAll());
        return "admin/employees/edit";
    }

    @PostMapping("/{id}/edit")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute Employee employee,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", departmentRepository.findAll());
            model.addAttribute("positions", positionRepository.findAll());
            return "admin/employees/edit";
        }
        Employee emp = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("社員が存在しません。"));

        emp.setEmployeeNumber(employee.getEmployeeNumber());
        emp.setName(employee.getName());
        emp.setEmail(employee.getEmail());
        emp.setPhone(employee.getPhone());
        emp.setJoinDate(employee.getJoinDate());
        emp.setDepartment(
                departmentRepository.findById(employee.getDepartment().getId())
                        .orElseThrow(() -> new IllegalArgumentException("部署が選択されてません。"))
        );
        emp.setPosition(
                positionRepository.findById(employee.getPosition().getId())
                        .orElseThrow(() -> new IllegalArgumentException("役職が選択されてません。"))
        );

        employeeRepository.save(emp);
        return "redirect:/admin/employees";
    }


    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        employeeRepository.deleteById(id);
        return "redirect:/admin/employees";
    }
}
