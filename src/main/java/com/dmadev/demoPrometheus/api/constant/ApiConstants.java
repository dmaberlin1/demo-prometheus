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


    //Based on user count on DB
    //75%
    public static final double GREEN_THRESHOLD = 75;

    //80%
    public static final double YELLOW_THRESHOLD = 80;

    //99%
    public static final double RED_THRESHOLD =99;

    //111%
    public static final double BLACK_THRESHOLD = 110;


}
