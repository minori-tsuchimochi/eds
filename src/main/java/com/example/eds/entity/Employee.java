package com.example.eds.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Entity
@Table(name = "employee")
@Getter
@Setter
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String employeeNumber;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotNull(message = "部署を選択してください")
    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @NotNull(message = "役職を選択してください")
    @ManyToOne
    @JoinColumn(name = "position_id", nullable = false)
    private Position position;

    @Email
    private String email;

    private String phone;

    @NotNull
    @Column(nullable = false)
    private LocalDate joinDate;
}
