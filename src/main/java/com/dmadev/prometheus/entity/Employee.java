package com.dmadev.prometheus.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "public",name="employees")
public final class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="first_name",length = 50)
    private String firstName;

    @Column(name="last_name",length = 50)
    private String lastName;

    @Column(name="hire_date")
    private LocalDateTime hireDate;

    @Column(name="salary",precision = 10,scale = 2)
    private BigDecimal salary;

}
