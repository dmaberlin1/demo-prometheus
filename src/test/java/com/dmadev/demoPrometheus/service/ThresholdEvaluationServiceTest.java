package com.dmadev.demoPrometheus.service;

import com.dmadev.demoPrometheus.api.constant.AlertLevel;
import com.dmadev.demoPrometheus.api.constant.ApiConstants;
import com.dmadev.demoPrometheus.client.AlertClient;
import com.dmadev.demoPrometheus.client.PrometheusClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class ThresholdEvaluationServiceTest {

    @Mock
    private PrometheusClient prometheusClient;

    @Mock
    private AlertClient alertService;

    @InjectMocks
    private ThresholdEvaluationService thresholdEvaluationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testGenerateAlertForGreenToYellow() throws IOException {
        // given
        double metricValue = ApiConstants.YELLOW_THRESHOLD + 1;
        when(prometheusClient.getMetricRequest()).thenReturn(metricValue);

        // when
        thresholdEvaluationService.evaluateAndGenerateAlert();

        // then
        verify(alertService).generateAlert(eq(AlertLevel.YELLOW), eq(metricValue), any(LocalDateTime.class));
    }

    @Test
    public void testDoNotGenerateAlertWhenStateImproves() throws IOException {
        // given
        double initialMetricValue = ApiConstants.YELLOW_THRESHOLD + 1;
        double improvedMetricValue = ApiConstants.GREEN_THRESHOLD + 1;

        when(prometheusClient.getMetricRequest()).thenReturn(initialMetricValue);
        thresholdEvaluationService.evaluateAndGenerateAlert(); // initial alert

        // when
        when(prometheusClient.getMetricRequest()).thenReturn(improvedMetricValue);
        thresholdEvaluationService.evaluateAndGenerateAlert(); // improved state

        // then
        verify(alertService, never()).generateAlert(eq(AlertLevel.GREEN), eq(improvedMetricValue), any(LocalDateTime.class));
    }

    @Test
    public void testGenerateAlertForImprovementAndDeterioration() throws IOException {
        // given
        double yellowThreshold = ApiConstants.YELLOW_THRESHOLD + 1;
        double greenThreshold = ApiConstants.GREEN_THRESHOLD + 1;
        double yellowAgainThreshold = ApiConstants.YELLOW_THRESHOLD + 1;

        // Первый вызов для YELLOW alert
        when(prometheusClient.getMetricRequest()).thenReturn(yellowThreshold);
        thresholdEvaluationService.evaluateAndGenerateAlert(); // YELLOW alert

        // Проверка на вызов метода generateAlert с уровнем YELLOW
        verify(alertService, times(1)).generateAlert(eq(AlertLevel.YELLOW), eq(yellowThreshold), any(LocalDateTime.class));

        // Второй вызов для GREEN alert
        when(prometheusClient.getMetricRequest()).thenReturn(greenThreshold);
        thresholdEvaluationService.evaluateAndGenerateAlert(); // GREEN alert

        // Проверка на вызов метода generateAlert с уровнем GREEN
        verify(alertService, times(0)).generateAlert(eq(AlertLevel.GREEN), eq(greenThreshold), any(LocalDateTime.class));

        // Снова установим метрику для YELLOW и вызовем evaluateAndGenerateAlert()
        when(prometheusClient.getMetricRequest()).thenReturn(yellowAgainThreshold);
        thresholdEvaluationService.evaluateAndGenerateAlert(); // YELLOW alert again

        // Проверка на вызов метода generateAlert с уровнем YELLOW снова
        verify(alertService, times(1)).generateAlert(eq(AlertLevel.YELLOW), eq(yellowAgainThreshold), any(LocalDateTime.class));

        // Убедитесь, что не было дополнительных вызовов generateAlert с GREEN
        verify(alertService, never()).generateAlert(eq(AlertLevel.GREEN), any(Double.class), any(LocalDateTime.class));
    }

    @Test
    public void testGenerateAlertForYellowToRed() throws IOException {
        // given
        double metricValue = ApiConstants.RED_THRESHOLD + 1;
        when(prometheusClient.getMetricRequest()).thenReturn(metricValue);

        // when
        thresholdEvaluationService.evaluateAndGenerateAlert();

        // then
        verify(alertService).generateAlert(eq(AlertLevel.RED), eq(metricValue), any(LocalDateTime.class));
    }



    @Test
    public void testGenerateAlertForRedToBlack() throws IOException {
        // given
        double metricValue = ApiConstants.BLACK_THRESHOLD + 1;
        when(prometheusClient.getMetricRequest()).thenReturn(metricValue);

        // when
        thresholdEvaluationService.evaluateAndGenerateAlert();

        // then
        verify(alertService).generateAlert(eq(AlertLevel.BLACK), eq(metricValue), any(LocalDateTime.class));
    }

}