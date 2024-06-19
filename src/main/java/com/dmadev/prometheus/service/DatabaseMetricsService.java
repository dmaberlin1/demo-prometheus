package com.dmadev.prometheus.service;

import com.dmadev.prometheus.repository.EmployeeRepository;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;


public interface DatabaseMetricsService {

    public void collectDatabaseMetrics();
}
