package com.dmadev.prometheus.service;

import com.dmadev.prometheus.api.response.DatabaseMetricResult;
import com.dmadev.prometheus.repository.DatabaseMetricsRepository;
import com.dmadev.prometheus.util.DatabaseCheckMetaData;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DefaultDatabaseMetricsService implements DatabaseMetricsService {


    private DatabaseMetricsRepository databaseMetricsRepository;

    @Scheduled(fixedRate = 60000)  // 60 sec -  to dev
    public void collectDatabaseMetrics() {
        List<DatabaseMetricResult> results = databaseMetricsRepository.executeMetricsQuery();
        DatabaseCheckMetaData.checkMetaData();
        results.forEach(result -> {
            String schemaName = result.getSchemaname();
            String tableName = result.getTablename();
            float fileSize = result.getFile_size_b();
            float dataSize = result.getData_size_b();
            long relTuples = result.getReltuples();
            long relPages = result.getRelpages();
            int bs = result.getBs();

            // Register additional metrics
            Metrics.gauge("custom.query.file.size", List.of(Tag.of("schema", schemaName), Tag.of("table", tableName)), fileSize);
            Metrics.gauge("custom.query.data.size", List.of(Tag.of("schema", schemaName), Tag.of("table", tableName)), dataSize);
            Metrics.gauge("custom.query.rel.tuples", List.of(Tag.of("schema", schemaName), Tag.of("table", tableName)), relTuples);
            Metrics.gauge("custom.query.rel.pages", List.of(Tag.of("schema", schemaName), Tag.of("table", tableName)), relPages);
            Metrics.gauge("custom.query.bs", List.of(Tag.of("schema", schemaName), Tag.of("table", tableName)), bs);
        });
    }


    // For  REST API
    public List<DatabaseMetricResult> getQueryResults() {
        return databaseMetricsRepository.executeMetricsQuery();
    }


    //eof
}



