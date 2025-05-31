package com.bjet.ems.controller;

import com.bjet.ems.entity.Employee;
import com.bjet.ems.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    //@PreAuthorize("hasRole('ROLE_USER')")
    public String getProfile(Principal principal, Model model) {
        String email = principal.getName();
        Employee employee = employeeService.getEmployeeByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found for email: " + email));
        model.addAttribute("employee", employee);
        return "employee-profile";
    }
}
