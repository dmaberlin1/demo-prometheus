package com.dmadev.prometheus.service;

import com.dmadev.prometheus.entity.Employee;
import com.dmadev.prometheus.repository.EmployeeRepository;
import com.dmadev.prometheus.util.DatabaseCheckMetaData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class DefaultEmployeeService implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Employee> getAllEmployees() {
        DatabaseCheckMetaData.checkMetaData();
        return employeeRepository.findAll();
    }
}
