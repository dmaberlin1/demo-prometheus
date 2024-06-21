package com.dmadev.prometheus.sheduler;

import com.dmadev.prometheus.service.DatabaseMetricsService;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class DatabaseMetricsSheduler {
    private final DatabaseMetricsService databaseMetricsService;


    @Scheduled(fixedRate = 30_000)  // 30 sec - for development
    public void updateMetrics(){
        databaseMetricsService.collectDatabaseMetrics();
    }
}
