package com.dmadev.demoPrometheus.service;

import com.dmadev.demoPrometheus.api.constant.ApiConstants;
import com.dmadev.demoPrometheus.dto.DatabaseMetricResult;
import com.dmadev.demoPrometheus.repository.DatabaseMetricsRepository;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;


import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


@Service
@Slf4j

public final class DatabaseMetricsService {

    private final DatabaseMetricsRepository databaseMetricsRepository;
    MeterRegistry meterRegistry;
    private final AtomicLong rowCountGauge;
    public AtomicLong rowCountGaugeAlter = new AtomicLong(0);


    public DatabaseMetricsService(MeterRegistry meterRegistry,
                                  DatabaseMetricsRepository databaseMetricsRepository
    ) {
        this.meterRegistry = meterRegistry;
        this.databaseMetricsRepository = databaseMetricsRepository;
        this.rowCountGauge = meterRegistry.gauge(ApiConstants.METRICS_QUERY_ROW_GAUGE,
                new AtomicLong(0));

    }

    public void collectDatabaseMetrics() {
        List<DatabaseMetricResult> results = databaseMetricsRepository.executeMetricsQuery();
        results.forEach(result -> {
            String schemaName = result.getSchemaname();
            String tableName = result.getTablename();
            Long rowCount = result.getRowCount();
            rowCountGaugeAlter.set(result.getRowCount());
            log.info(String.format("Schema: %s, Table: %s, Row Count: %,d", schemaName, tableName, rowCount));
            this.meterRegistry.counter(ApiConstants.METRICS_REQUEST_IN_DB_FROM_DATABASE_METRICS_SERVICE_COUNT, List.of(
                    Tag.of("schemaName", schemaName), Tag.of("tableName", tableName)
            )).increment();
            rowCountGauge.set(rowCount);
        });
    }

    // For  REST API
    public List<DatabaseMetricResult> getQueryResults() {
        return databaseMetricsRepository.executeMetricsQuery();
    }

}



