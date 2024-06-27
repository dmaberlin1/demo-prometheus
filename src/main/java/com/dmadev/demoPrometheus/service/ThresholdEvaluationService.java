package com.dmadev.demoPrometheus.service;

import com.dmadev.demoPrometheus.api.constant.AlertLevel;
import com.dmadev.demoPrometheus.api.constant.ApiConstants;
import com.dmadev.demoPrometheus.client.AlertClient;
import com.dmadev.demoPrometheus.client.PrometheusClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

@Service
@Slf4j
@RequiredArgsConstructor
public final class ThresholdEvaluationService {

    private final PrometheusClient prometheusClient;
    private final AlertClient alertService;

    private AlertLevel previousAlertLevel = AlertLevel.NONE;


    //methods for map navigation: floorEntry, ceilingEntry, lowerEntry, higherEntry
    private static final NavigableMap<Double, AlertLevel> thresholds = new TreeMap<Double, AlertLevel>() {
        {
            put(ApiConstants.GREEN_THRESHOLD, AlertLevel.GREEN);
            put(ApiConstants.YELLOW_THRESHOLD, AlertLevel.YELLOW);
            put(ApiConstants.RED_THRESHOLD, AlertLevel.RED);
            put(ApiConstants.BLACK_THRESHOLD, AlertLevel.BLACK);
        }
    };


    /**
     * Method for estimating the current value of the metric and generating alerts if necessary
     */
    public void evaluateAndGenerateAlert() {
        double currentValue;
        try {
            currentValue = prometheusClient.getMetricRequest();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        AlertLevel currentAlertLevel = determineAlertLevel(currentValue);

        if (shouldGenerateAlert(currentAlertLevel)) {
            generateAlert(currentAlertLevel, currentValue);
            previousAlertLevel = currentAlertLevel;
        }
    }

    /**
     * Method for determining the alert level based on the value of a metric
     *
     * @param value metric value
     * @return alert level
     */
    private AlertLevel determineAlertLevel(double value) {
        //запись с наибольшим ключом, который меньше или равен данному значению
        Map.Entry<Double, AlertLevel> entry = thresholds.floorEntry(value);
        // return уровень алерта, если запись найдена, иначе возвращаем уровень NONE
        return entry != null ? entry.getValue() : AlertLevel.NONE;
    }


    /**
     * Method for generating an alert
     *
     * @param alertLevel alert level
     * @param value metric value
     */
    private void generateAlert(AlertLevel alertLevel, double value) {
        alertService.generateAlert(alertLevel, value, LocalDateTime.now());
    }

    /**
     * Method to check if an alert should be generated
     *
     * @param currentAlertLevel current alert level
     * @return true if it is necessary to generate an alert, otherwise false
     */
    private boolean shouldGenerateAlert(AlertLevel currentAlertLevel) {
        return currentAlertLevel != previousAlertLevel &&
                (previousAlertLevel == AlertLevel.NONE ||
                        currentAlertLevel.getLevel() > previousAlertLevel.getLevel()
                );
    }
}


