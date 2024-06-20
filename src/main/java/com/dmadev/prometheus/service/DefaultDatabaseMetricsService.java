package com.dmadev.prometheus.service;

import com.dmadev.prometheus.dto.DatabaseMetricResult;
import com.dmadev.prometheus.repository.DatabaseMetricsRepository;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import java.util.List;

@RequiredArgsConstructor
@Service
public class DefaultDatabaseMetricsService implements DatabaseMetricsService {


    private final DatabaseMetricsRepository databaseMetricsRepository;

    @Scheduled(fixedRate = 60000)  // 60 sec - for development
    public void collectDatabaseMetrics() {
        List<DatabaseMetricResult> results = databaseMetricsRepository.executeMetricsQuery();
        results.forEach(result -> {
            String schemaName = result.getSchemaname();
            String tableName = result.getTablename();
            Long rowCount = result.getRowCount();
            String[] parts = result.getTableSize().split("\\s+");
            Long tableSize = Long.getLong(parts[0]);
            String unit = parts[1].trim();

            // Register additional metrics
            Metrics.gauge("custom.query.row.count", List.of(Tag.of("schema", schemaName), Tag.of("table", tableName)), rowCount);
            Metrics.gauge("custom.query.table.size", List.of(Tag.of("schema", schemaName), Tag.of("table", tableName),Tag.of("unit",unit)),tableSize );
        });
    }

    // For  REST API
    public List<DatabaseMetricResult> getQueryResults() {
        return databaseMetricsRepository.executeMetricsQuery();
    }


    //eof
}



