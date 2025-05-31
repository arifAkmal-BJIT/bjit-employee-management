package com.bjet.ems.controller;

import com.bjet.ems.entity.Employee;
import com.bjet.ems.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/{id}")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getEmployeeDetails(@PathVariable Long id, Model model) {
        Employee employee = employeeService.getEmployeeById(id);
        model.addAttribute("employee", employee);
        return "employee-details";
    }

    @GetMapping
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getEmployeeList(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(defaultValue = "id") String sortBy,
                                  Model model) {
        Page<Employee> employees = employeeService.getAllEmployees(page, size, sortBy);
        model.addAttribute("employees", employees);
        return "employee-list";
    }

    @GetMapping("/new")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public String createEmployeeForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "employee-form";
    }

    @PostMapping
    // @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String saveEmployee(@Valid @ModelAttribute Employee employee, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "employee-form";
        }
        employeeService.saveEmployee(employee);
        return "redirect:/admin/employees";
    }

    @GetMapping("/edit/{id}")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editEmployeeForm(@PathVariable Long id, Model model) {
        Employee employee = employeeService.getEmployeeById(id);
        model.addAttribute("employee", employee);
        return "employee-form";
    }

    @PostMapping("/update")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateEmployee(@Valid @ModelAttribute Employee employee, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "employee-form";
        }
        employeeService.updateEmployee(employee);
        return "redirect:/admin/employees";
    }

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return "redirect:/admin/employees";
    }
}
