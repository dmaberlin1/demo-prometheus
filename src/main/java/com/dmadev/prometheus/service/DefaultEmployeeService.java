package com.dmadev.prometheus.service;

import com.dmadev.prometheus.entity.Employee;
import com.dmadev.prometheus.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class DefaultEmployeeService implements EmployeeService {

    private final EmployeeRepository employeeRepository;


    @Transactional(readOnly = true)
    public List<Object[]> executeAndLogQueryResults() {
        List<Object[]> queryResults = employeeRepository.executeMetricsQuery();
        for (Object[] result : queryResults) {
            log.debug("Query result: {}", Arrays.toString(result));
        }
        return queryResults;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

}
