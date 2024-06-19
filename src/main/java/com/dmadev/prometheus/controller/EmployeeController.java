package com.dmadev.prometheus.controller;

import com.dmadev.prometheus.entity.Employee;
import com.dmadev.prometheus.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    //TODO контроллер /metrics

    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/execute-query")
    public ResponseEntity<String> executeAndReturnQueryResults() {
        try {
            List<Object[]> queryResults = employeeService.executeAndLogQueryResults();

            // Собираем все строки лога в одну строку
            String logMessages = queryResults.stream()
                    .map(result -> "Query result: " + Arrays.toString(result))
                    .collect(Collectors.joining("\n"));
            return ResponseEntity.ok(logMessages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
