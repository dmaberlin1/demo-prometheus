package com.dmadev.prometheus.repository;

import com.dmadev.prometheus.api.response.DatabaseMetricResult;

import java.util.List;

public interface DatabaseMetricsRepository {

    List<DatabaseMetricResult> executeMetricsQuery();
}
