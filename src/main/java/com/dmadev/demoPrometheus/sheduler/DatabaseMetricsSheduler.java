package com.dmadev.demoPrometheus.sheduler;

import com.dmadev.demoPrometheus.service.DatabaseMetricsService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class DatabaseMetricsSheduler {
    private final DatabaseMetricsService databaseMetricsService;

    @Scheduled(fixedRate = 30_000)  // 30 sec - for development
    private void updateMetrics() {
        databaseMetricsService.collectDatabaseMetrics();
    }
}
