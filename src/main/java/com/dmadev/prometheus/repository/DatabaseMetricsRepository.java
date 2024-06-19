package com.dmadev.prometheus.repository;

import com.dmadev.prometheus.api.response.DatabaseMetricResult;
import com.dmadev.prometheus.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;

public interface DatabaseMetricsRepository {

        List<DatabaseMetricResult> executeMetricsQuery();
}
