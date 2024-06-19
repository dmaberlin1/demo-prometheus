package com.dmadev.prometheus.service;

import com.dmadev.prometheus.api.response.DatabaseMetricResult;
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


     @Scheduled(fixedRate = 60000)  // 60 sec -  to dev
//    @Scheduled(fixedRate = 600000) //10 minutes - for testing
    public void collectDatabaseMetrics() {
        List<DatabaseMetricResult> results = databaseMetricsRepository.executeMetricsQuery();

        results.forEach(result -> {
            String schemaName = result.getSchemaname();
            String tableName = result.getTablename();
            float fileSize = result.getFile_size_b();
            float dataSize = result.getData_size_b();
            long relTuples = result.getReltuples();
            long relPages = result.getRelpages();
            int bs = result.getBs();

            // Register file size as a gauge metric
            Metrics.gauge("custom.query.file.size", List.of(Tag.of("schema", schemaName), Tag.of("table", tableName)), fileSize);
            // Register data size as a gauge metric
            Metrics.gauge("custom.query.data.size", List.of(Tag.of("schema", schemaName), Tag.of("table", tableName)), dataSize);

            // Register additional metrics
            Metrics.gauge("custom.query.rel.tuples", List.of(Tag.of("schema", schemaName), Tag.of("table", tableName)), relTuples);
            Metrics.gauge("custom.query.rel.pages", List.of(Tag.of("schema", schemaName), Tag.of("table", tableName)), relPages);
            Metrics.gauge("custom.query.bs", List.of(Tag.of("schema", schemaName), Tag.of("table", tableName)), bs);
        });
    }


    // Метод для получения результатов запроса
    public List<DatabaseMetricResult> getQueryResults() {
        return databaseMetricsRepository.executeMetricsQuery();
    }


    //eof
}



