package com.bjet.ems.service.impl;// ...existing code...

import com.bjet.ems.entity.Employee;
import com.bjet.ems.repository.EmployeeRepository;
import com.bjet.ems.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.valueOf(id)));
    }

    @Override
    public Page<Employee> getAllEmployees(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return employeeRepository.findAll(pageable);
    }

    @Override
    public void saveEmployee(Employee employee) {
        employee.setLastModifiedAt(LocalDateTime.now());
        employeeRepository.save(employee);
    }

    @Override
    public void updateEmployee(Employee employee) {
        if (!employeeRepository.existsById(employee.getId())) {
            throw new RuntimeException(String.valueOf(employee.getId()));
        }
        employee.setLastModifiedAt(LocalDateTime.now());
        employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException(String.valueOf(id));
        }
        employeeRepository.deleteById(id);
    }

    @Override
    public Optional<Employee> getEmployeeByEmail(String email) {
        return employeeRepository.findEmployeeByEmail(email).stream().findFirst();
    }
}
