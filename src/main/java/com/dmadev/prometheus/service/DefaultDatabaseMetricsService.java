package com.dmadev.prometheus.service;

import com.dmadev.prometheus.dto.DatabaseMetricResult;
import com.dmadev.prometheus.repository.DatabaseMetricsRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


@Service
@Slf4j
//@RequiredArgsConstructor
public class DefaultDatabaseMetricsService implements DatabaseMetricsService {

    private final DatabaseMetricsRepository databaseMetricsRepository;

    private final AtomicLong rowCountGauge;

//    @Autowired
//    public void MetricsService(MeterRegistry meterRegistry){
//        this.rowCountGauge = meterRegistry.gauge("custom_query_row_count",
//                new AtomicLong(0));
//    }

    public DefaultDatabaseMetricsService(MeterRegistry meterRegistry ,
                                         DatabaseMetricsRepository databaseMetricsRepository
                                        ) {
        this.databaseMetricsRepository = databaseMetricsRepository;
        this.rowCountGauge = meterRegistry.gauge("custom_query_row_count",
                new AtomicLong(0));

    }

    public void collectDatabaseMetrics() {
        List<DatabaseMetricResult> results = databaseMetricsRepository.executeMetricsQuery();
        results.forEach(result -> {
            String schemaName = result.getSchemaname();
            String tableName = result.getTablename();
            Long rowCount = result.getRowCount();
            log.info(String.format("Schema: %s, Table: %s, Row Count: %,d", schemaName, tableName, rowCount));

            rowCountGauge.set(rowCount);

        });
    }

    // For  REST API
    public List<DatabaseMetricResult> getQueryResults() {
        return databaseMetricsRepository.executeMetricsQuery();
    }



}



