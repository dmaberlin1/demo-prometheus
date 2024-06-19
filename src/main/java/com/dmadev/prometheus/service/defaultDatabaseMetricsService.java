package com.dmadev.prometheus.service;

import com.dmadev.prometheus.repository.EmployeeRepository;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class defaultDatabaseMetricsService implements DatabaseMetricsService {
    private final EmployeeRepository employeeRepository;


    @Scheduled(fixedRate = 60000)
    public void collectDatabaseMetrics() {
        List<Object[]> results = employeeRepository.executeMetricsQuery();


        // Register metrics via MeterRegistry (Micrometer)
        results.forEach(result -> {
            String schemaName = (String) result[0];
            String tableName = (String) result[1];
            float fileSize = (float) result[4];
            float dataSize = (float) result[5];

            // Register file size as a gauge metric
            Metrics.gauge("custom.query.file.size", List.of(Tag.of("schema", schemaName), Tag.of("table", tableName)), fileSize);
            // Register data size as a gauge metric
            Metrics.gauge("custom.query.data.size", List.of(Tag.of("schema", schemaName), Tag.of("table", tableName)), dataSize);
        });
    }


    //eof
}



