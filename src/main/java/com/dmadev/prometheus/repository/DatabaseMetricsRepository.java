package com.dmadev.prometheus.repository;

import com.dmadev.prometheus.dto.DatabaseMetricResult;
import io.micrometer.core.annotation.Timed;

import java.util.List;

public interface DatabaseMetricsRepository {

    List<DatabaseMetricResult> executeMetricsQuery();
}
