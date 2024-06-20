package com.dmadev.prometheus.controller;

import com.dmadev.prometheus.dto.DatabaseMetricResult;
import com.dmadev.prometheus.service.DatabaseMetricsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/metrics")
@RequiredArgsConstructor
public class MetricsController {

    private final DatabaseMetricsService databaseMetricsService;

    @GetMapping("/query")
    public ResponseEntity<String> executeAndReturnQueryResults() {
        try {
            List<DatabaseMetricResult> queryResults = databaseMetricsService.getQueryResults();
            String logMessages = queryResults.stream()
                    .map(DatabaseMetricResult::toString)
                    .collect(Collectors.joining("\n"));
            return ResponseEntity.ok(logMessages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
