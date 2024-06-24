package com.dmadev.demoPrometheus.controller;

import com.dmadev.demoPrometheus.api.constant.ApiConstants;
import com.dmadev.demoPrometheus.client.PrometheusClient;
import com.dmadev.demoPrometheus.dto.DatabaseMetricResult;
import com.dmadev.demoPrometheus.service.DatabaseMetricsService;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/metrics")
public class MetricsController {

    private static final Logger log = LoggerFactory.getLogger(MetricsController.class);
    private final DatabaseMetricsService databaseMetricsService;
    PrometheusClient prometheusClient;
    MeterRegistry meterRegistry;

    public MetricsController(DatabaseMetricsService databaseMetricsService, MeterRegistry meterRegistry,PrometheusClient prometheusClient) {
        this.databaseMetricsService = databaseMetricsService;
        this.meterRegistry = meterRegistry;
        this.prometheusClient=prometheusClient;
    }

    @GetMapping("/query")
    public ResponseEntity<String> executeAndReturnQueryResults() {
        try {
//            prometheusClient.doGetRequest();
            prometheusClient.getMetricRequest();
        } catch (IOException e) {
            log.warn(e.toString());

        }

        this.meterRegistry.counter(ApiConstants.METRICS_CONTROLLER_REST_COUNT,List.of())
                .increment();
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
