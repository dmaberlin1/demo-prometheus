package com.dmadev.prometheus.service;

import com.dmadev.prometheus.dto.DatabaseMetricResult;
import com.dmadev.prometheus.repository.DatabaseMetricsRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultDatabaseMetricsService implements DatabaseMetricsService {

    private final DatabaseMetricsRepository databaseMetricsRepository;


    private final MeterRegistry meterRegistry;

    private AtomicLong rowCountGaugeMain;
    private final AtomicLong rowCountGauge = new AtomicLong(0);


    @PostConstruct
    public void initialize() {
        rowCountGaugeMain = meterRegistry.gauge("custom.query.row.count", new AtomicLong(0));
        Gauge.builder("custom.query.row.count1", rowCountGauge, AtomicLong::get)
                .description("Number of rows in the table")
                .register(meterRegistry);
    }

    public void collectDatabaseMetrics() {
        List<DatabaseMetricResult> results = databaseMetricsRepository.executeMetricsQuery();
        results.forEach(result -> {
            String schemaName = result.getSchemaname();
            String tableName = result.getTablename();
            Long rowCount = result.getRowCount();
            log.info(String.format("Schema: %s, Table: %s, Row Count: %,d", schemaName, tableName, rowCount));

            rowCountGaugeMain.set(rowCount);
            rowCountGauge.set(rowCount);

        });
    }

    public List<DatabaseMetricResult> getQueryResults() {
        return databaseMetricsRepository.executeMetricsQuery();
    }

}



