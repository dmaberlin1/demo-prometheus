package com.dmadev.demoPrometheus.service;

import com.dmadev.demoPrometheus.api.constant.AlertLevel;
import com.dmadev.demoPrometheus.dto.Alert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public final class AlertService {


   public void generateAlert(AlertLevel alertLevel, double value, LocalDateTime timestamp){
      Alert alert=new Alert();
       alert.setAlertLevel(alertLevel);
       alert.setValue(alertLevel.ordinal());
       alert.setTimestamp(timestamp);

       log.info("Generated alert: {}", alert);
      // Здесь можно добавить логику отправки алертов в систему уведомлений или сохранения в базу данных
   }
}
