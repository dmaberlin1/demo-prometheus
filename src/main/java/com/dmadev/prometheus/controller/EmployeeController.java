package com.dmadev.prometheus.controller;

import com.dmadev.prometheus.service.DefaultEmployeeService;
import com.dmadev.prometheus.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    //TODO контроллер /metrics
}
