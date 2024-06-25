package com.dmadev.demoPrometheus.config;

import com.dmadev.demoPrometheus.api.constant.ApiConstants;
import com.dmadev.demoPrometheus.service.DatabaseMetricsService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.context.annotation.Bean;


public class MetricsBean {

    @Bean
    MeterBinder meterBinder(DatabaseMetricsService databaseMetricsService) {
        return meterRegistry -> {
            Counter.builder(ApiConstants.METRICS_CONTROLLER_REST_COUNT)
                    .description("Count of requests to Metrics Controller")
                    .register(meterRegistry);

            Counter.builder(ApiConstants.METRICS_DATABASE_EMPLOYEES_COUNT)
                    .description("Count of requests to Employees table")
                    .register(meterRegistry);

            Counter.builder(ApiConstants.METRICS_REQUEST_IN_DB_FROM_DATABASE_METRICS_SERVICE_COUNT)
                    .description("Count of requests to db from DatabaseMetricsService")
                    .tag("schemaName", "tableName")
                    .register(meterRegistry);

            //TODO not working- find a solution
            Gauge.builder(ApiConstants.METRICS_QUERY_ROW_ALTER_GAUGE, () -> databaseMetricsService.rowCountGaugeAlter.doubleValue())
                    .description("Alter number of rows in the table employees")
                    .register(meterRegistry);

        };
    }
}
