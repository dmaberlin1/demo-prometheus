package com.dmadev.prometheus.repository;


import java.util.List;

import com.dmadev.prometheus.dto.DatabaseMetricResult;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class DefaultDatabaseMetricsRepository implements DatabaseMetricsRepository {

    private final JdbcTemplate jdbcTemplate;

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


    @Override
    @Timed
    public List<DatabaseMetricResult> executeMetricsQuery() {
        return jdbcTemplate.query(SQL_QUERY, new BeanPropertyRowMapper<>(DatabaseMetricResult.class));
    }

}
