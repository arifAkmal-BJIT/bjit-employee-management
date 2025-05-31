package com.bjet.ems.service;

import com.bjet.ems.entity.Employee;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface EmployeeService {
    Employee getEmployeeById(Long id);
    Page<Employee> getAllEmployees(int page, int size, String sortBy);
    void saveEmployee(Employee employee);
    void updateEmployee(Employee employee);
    void deleteEmployee(Long id);
    Optional<Employee> getEmployeeByEmail(String email);
}
