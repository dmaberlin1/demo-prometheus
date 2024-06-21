package com.dmadev.prometheus.service;

import com.dmadev.prometheus.dto.DatabaseMetricResult;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface DatabaseMetricsService {

    public List<DatabaseMetricResult> getQueryResults();
    public void collectDatabaseMetrics();

    }
