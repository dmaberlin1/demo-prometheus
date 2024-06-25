package com.dmadev.demoPrometheus.service;

import com.dmadev.demoPrometheus.api.constant.AlertLevel;
import com.dmadev.demoPrometheus.dto.AlertDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
@Slf4j
public final class AlertService {

    private final RestTemplate restTemplate;
    private final String alertAppUrl;
    String alerts_uri = "/alerts";

    public AlertService(RestTemplate restTemplate,
                        @Value("${alert.app.url}") String alertAppUrl) {
        this.restTemplate = restTemplate;
        this.alertAppUrl = alertAppUrl;
    }


    public void generateAlert(AlertLevel alertLevel, double value, LocalDateTime timestamp) {

        AlertDTO alert = new AlertDTO(alertLevel, value, timestamp);
        sendAlert(alert);
    }

    public void sendAlert(AlertDTO alert) {

        restTemplate.postForObject(alertAppUrl + alerts_uri, alert, Void.class);
    }
}
