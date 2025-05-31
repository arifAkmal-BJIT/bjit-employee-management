package com.bjet.ems.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Email
    @Size(max = 100)
    private String email;

    @NotBlank
    @Size(max = 50)
    private String designation;

    @NotNull
    private LocalDate joiningDate;

    private boolean activeStatus;

    private LocalDateTime lastModifiedAt;

    // Getters and Setters
    // ...existing code...
}
