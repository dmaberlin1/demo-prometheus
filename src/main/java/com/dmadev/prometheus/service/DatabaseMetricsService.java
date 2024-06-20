package com.dmadev.prometheus.service;

import com.dmadev.prometheus.dto.DatabaseMetricResult;

import java.util.List;


public interface DatabaseMetricsService {

    public List<DatabaseMetricResult> getQueryResults();
}
