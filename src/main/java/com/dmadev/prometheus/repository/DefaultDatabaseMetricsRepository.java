package com.dmadev.prometheus.repository;

import java.util.ArrayList;
import java.util.List;

import com.dmadev.prometheus.api.response.DatabaseMetricResult;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
@Primary //  - для дебага, поочереди меняем c SecondaryDatabaseMetricsRepository
public class DefaultDatabaseMetricsRepository implements DatabaseMetricsRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String SQL_QUERY = "SELECT rs.schemaname, tablename, cc.reltuples, cc.relpages, bs, "
            + "(pg_relation_size(pst.relid))::FLOAT AS file_size_b, "
            + "(CEIL((pst.n_live_tup*((datahdr+ma-(CASE WHEN datahdr%ma=0 THEN ma ELSE datahdr%ma END))+nullhdr2+4))/(bs-20::FLOAT))::FLOAT * bs) AS data_size_b "
            + "FROM ( SELECT ma,bs,schemaname,tablename, "
            + "(datawidth+(hdr+ma-(CASE WHEN hdr%ma=0 THEN ma ELSE hdr%ma END)))::NUMERIC AS datahdr, "
            + "(maxfracsum*(nullhdr+ma-(CASE WHEN nullhdr%ma=0 THEN ma ELSE nullhdr%ma END))) AS nullhdr2 "
            + "FROM ( SELECT schemaname, tablename, hdr, ma, bs, "
            + "SUM((1-null_frac)*avg_width) AS datawidth, "
            + "MAX(null_frac) AS maxfracsum, "
            + "hdr+(SELECT 1+COUNT(*)/8 FROM pg_stats s2 "
            + "WHERE null_frac<>0 AND s2.schemaname = s.schemaname AND s2.tablename = s.tablename "
            + ") AS nullhdr FROM pg_stats s, ( "
            + "SELECT (SELECT current_setting('block_size')::NUMERIC) AS bs, "
            + "CASE WHEN SUBSTRING(v,12,3) IN ('8.0','8.1','8.2') THEN 27 ELSE 23 END AS hdr, "
            + "CASE WHEN v ~ 'mingw32' THEN 8 ELSE 4 END AS ma, v "
            + "FROM (SELECT version() AS v) AS foo "
            + ") AS constants GROUP BY 1,2,3,4,5 "
            + ") AS foo ) AS rs "
            + "JOIN pg_stat_user_tables pst ON rs.tablename = pst.relname "
            + "JOIN pg_class cc ON cc.oid = pst.relid "
            + "JOIN pg_namespace nn ON cc.relnamespace = nn.oid "
            + "AND nn.nspname = rs.schemaname AND nn.nspname <> 'information_schema';";


    @Override
    public   List<DatabaseMetricResult> executeMetricsQuery() {
        return jdbcTemplate.query(SQL_QUERY, rs -> {
            List<DatabaseMetricResult> results = new ArrayList<>();
            while (rs.next()) {
                DatabaseMetricResult result = new DatabaseMetricResult();
                result.setSchemaname(rs.getString("schemaname"));
                result.setTablename(rs.getString("tablename"));
                result.setReltuples(rs.getLong("reltuples"));
                result.setRelpages(rs.getLong("relpages"));
                result.setBs(rs.getInt("bs"));
                result.setFile_size_b(rs.getFloat("file_size_b"));
                result.setData_size_b(rs.getFloat("data_size_b"));
                results.add(result);
            }
            return results;
        });
    }

//  @Override   //
//    public List<DatabaseMetricResult> executeMetricsQuery() {
//        return jdbcTemplate.query(SQL_QUERY, new BeanPropertyRowMapper<>(DatabaseMetricResult.class));
//    }


    //eof
}
