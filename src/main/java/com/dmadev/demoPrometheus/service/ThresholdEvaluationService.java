package com.dmadev.demoPrometheus.service;

import com.dmadev.demoPrometheus.api.constant.AlertLevel;
import com.dmadev.demoPrometheus.client.PrometheusClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class ThresholdEvaluationService {

    private final PrometheusClient prometheusClient;
    private final AlertService alertService;


    public void evaluateAndGenerateAlert() {
        double currentValue;
        try {
           currentValue = prometheusClient.doGetRequestAlter();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (currentValue >= 1.11) {
            alertService.generateAlert(AlertLevel.BLACK, currentValue, LocalDateTime.now());
        } else if (currentValue >= 1.00 && currentValue <= 1.10) {
            alertService.generateAlert(AlertLevel.RED, currentValue, LocalDateTime.now());
        } else if (currentValue >= 0.80 && currentValue < 0.99) {
            alertService.generateAlert(AlertLevel.YELLOW, currentValue, LocalDateTime.now());
        } else if (currentValue >= 0.75 && currentValue < 0.80) {
            alertService.generateAlert(AlertLevel.GREEN, currentValue, LocalDateTime.now());
        }
    }
}

