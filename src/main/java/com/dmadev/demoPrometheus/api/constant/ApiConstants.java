package com.dmadev.demoPrometheus.api.constant;

import lombok.Getter;

@Getter
public class ApiConstants {
    public static final String METRICS_CONTROLLER_REST_COUNT = "metrics_controller_rest_count";
    public static final String METRICS_DATABASE_EMPLOYEES_COUNT = "metrics_database_employees_count";
    public static final String METRICS_REQUEST_IN_DB_FROM_DATABASE_METRICS_SERVICE_COUNT =
            "metrics_request_in_db_from_database_metrics_service";
    public static final String METRICS_QUERY_ROW_GAUGE = "metrics_query_row_count_gauge";
    public static final String METRICS_QUERY_ROW_ALTER_GAUGE = "metrics_query_row_count_gauge_1";

    private static final double GREEN_THRESHOLD = 0.75;
    private static final double YELLOW_THRESHOLD = 0.8;
    private static final double RED_THRESHOLD_LOW = 0.99;
    private static final double RED_THRESHOLD_HIGH = 1.1;


}
