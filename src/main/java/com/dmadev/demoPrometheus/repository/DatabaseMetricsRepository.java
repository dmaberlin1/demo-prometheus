package com.dmadev.demoPrometheus.repository;


import java.util.List;


import com.dmadev.demoPrometheus.api.constant.ApiConstants;
import com.dmadev.demoPrometheus.dto.DatabaseMetricResult;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class DatabaseMetricsRepository{

    private final JdbcTemplate jdbcTemplate;
    MeterRegistry meterRegistry;

    public DatabaseMetricsRepository(JdbcTemplate jdbcTemplate, MeterRegistry meterRegistry) {
        this.jdbcTemplate = jdbcTemplate;
        this.meterRegistry = meterRegistry;
    }

    private static final String SQL_QUERY ="SELECT\n" +
            "    n.nspname AS schemaname,\n" +
            "    c.relname AS tablename,\n" +
            "    (SELECT count(*) FROM employees) AS row_count\n" +
            "FROM\n" +
            "    pg_class c\n" +
            "        JOIN pg_namespace n ON n.oid = c.relnamespace\n" +
            "WHERE\n" +
            "    c.relkind IN ('r', 'm')\n" +
            "  AND n.nspname NOT IN ('pg_catalog', 'information_schema')\n" +
            "  AND c.relname = 'employees'";


    public List<DatabaseMetricResult> executeMetricsQuery() {
        this.meterRegistry.counter(ApiConstants.METRICS_DATABASE_EMPLOYEES_COUNT,List.of()).increment();
        return jdbcTemplate.query(SQL_QUERY, new BeanPropertyRowMapper<>(DatabaseMetricResult.class));
    }

}
