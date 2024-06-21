package com.dmadev.demoPrometheus.dto;

import com.dmadev.demoPrometheus.api.constant.AlertLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alert {
    private AlertLevel alertLevel;
    private double value;
    private LocalDateTime timestamp;
}