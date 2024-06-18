package com.dmadev.prometheus.service;

import com.dmadev.prometheus.repository.EmployeeRepository;
import io.prometheus.client.CollectorRegistry;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class DatabaseMetricsService {
    private final EmployeeRepository employeeRepository;
    // Пример метрик для размера данных и количества записей
    private final Counter dataSizeMetric = Counter.build()
            .name("data_size_bytes")
            .help("Size of data in bytes")
            .labelNames("schema", "table")
            .register();

}
