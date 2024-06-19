package com.dmadev.prometheus.service;

import com.dmadev.prometheus.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class DefaultEmployeeService implements EmployeeService {

    private final EmployeeRepository employeeRepository;


    public void executeAndLogQueryResults() {
        List<Object[]> queryResults = employeeRepository.executeMetricsQuery();
        for (Object[] result : queryResults) {
            log.info("Query result: {}", Arrays.toString(result));
        }
    }

}
