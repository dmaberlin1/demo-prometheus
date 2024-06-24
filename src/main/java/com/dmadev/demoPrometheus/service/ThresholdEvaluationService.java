package com.dmadev.demoPrometheus.service;

import com.dmadev.demoPrometheus.api.constant.AlertLevel;
import com.dmadev.demoPrometheus.api.constant.ApiConstants;
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
    private final AlertService alertService;

    private AlertLevel previousAlertLevel = AlertLevel.NONE;


    //методы для навигации по карте: floorEntry, ceilingEntry, lowerEntry, и higherEntry
    private static final NavigableMap<Double, AlertLevel> thresholds = new TreeMap<Double, AlertLevel>() {
        {
            put(ApiConstants.GREEN_THRESHOLD, AlertLevel.GREEN);
            put(ApiConstants.YELLOW_THRESHOLD, AlertLevel.YELLOW);
            put(ApiConstants.RED_THRESHOLD, AlertLevel.RED);
            put(ApiConstants.BLACK_THRESHOLD, AlertLevel.BLACK);
        }
        };


    /**
     * Метод для оценки текущего значения метрики и генерации алертов при необходимости
     */
    public void evaluateAndGenerateAlert() {
        double currentValue;
        try {
            // Получение текущего значения метрики от клиента Prometheus
            currentValue = prometheusClient.getMetricRequest();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Определение текущего уровня алерта на основе значения метрики
        AlertLevel currentAlertLevel = determineAlertLevel(currentValue);

        // Проверка, изменился ли уровень алерта и не является ли он NONE
        if (shouldGenerateAlert(currentAlertLevel)) {
            // Генерация алерта при изменении уровня
            generateAlert(currentAlertLevel, currentValue);

            previousAlertLevel = currentAlertLevel;
        }
    }

    /**
     * Метод для определения уровня алерта на основе значения метрики
     *
     * @param value значение метрики
     * @return уровень алерта
     */
    private AlertLevel determineAlertLevel(double value) {
        //запись с наибольшим ключом, который меньше или равен данному значению
        Map.Entry<Double, AlertLevel> entry = thresholds.floorEntry(value);
        // Возвращаем уровень алерта, если запись найдена, иначе возвращаем уровень NONE
        return entry != null ? entry.getValue() : AlertLevel.NONE;
    }


    /**
     * Метод для генерации алерта
     *
     * @param alertLevel уровень алерта
     * @param value значение метрики
     */
    private void generateAlert(AlertLevel alertLevel, double value) {
        // Вызов метода alertService для генерации алерта
        alertService.generateAlert(alertLevel, value, LocalDateTime.now());
    }

    /**
     * Метод для проверки необходимости генерации алерта
     *
     * @param currentAlertLevel текущий уровень алерта
     * @return true если необходимо сгенерировать алерт, иначе false
     */
    private boolean shouldGenerateAlert(AlertLevel currentAlertLevel) {
        // Генерация алерта, если текущий уровень выше предыдущего уровня or previous is none

        return currentAlertLevel != previousAlertLevel &&
                (previousAlertLevel == AlertLevel.NONE || currentAlertLevel.compareTo(previousAlertLevel) > 0);
    }
}


