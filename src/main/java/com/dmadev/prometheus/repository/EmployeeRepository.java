package com.dmadev.prometheus.repository;

import com.dmadev.prometheus.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query(nativeQuery = true, value = "SELECT rs.schemaname, tablename,\n" +
            "       cc.reltuples, cc.relpages, bs,\n" +
            "       (pg_relation_size(pst.relid))::FLOAT AS file_size_b,\n" +
            "       (CEIL((pst.n_live_tup*((datahdr+ma-(CASE WHEN datahdr%ma=0 THEN ma ELSE datahdr%ma END))+nullhdr2+4))/(bs-20::FLOAT))::FLOAT * bs) AS data_size_b\n" +
            "FROM (\n" +
            "         SELECT ma,bs,schemaname,tablename,\n" +
            "                (datawidth+(hdr+ma-(CASE WHEN hdr%ma=0 THEN ma ELSE hdr%ma END)))::NUMERIC AS datahdr,\n" +
            "                (maxfracsum*(nullhdr+ma-(CASE WHEN nullhdr%ma=0 THEN ma ELSE nullhdr%ma END))) AS nullhdr2\n" +
            "         FROM (\n" +
            "                  SELECT schemaname, tablename, hdr, ma, bs,\n" +
            "                         SUM((1-null_frac)*avg_width) AS datawidth,\n" +
            "                         MAX(null_frac) AS maxfracsum,\n" +
            "                         hdr+(SELECT 1+COUNT(*)/8\n" +
            "                              FROM pg_stats s2\n" +
            "                              WHERE null_frac<>0 AND s2.schemaname = s.schemaname AND s2.tablename = s.tablename\n" +
            "                         ) AS nullhdr\n" +
            "                  FROM pg_stats s, (\n" +
            "                      SELECT\n" +
            "                          (SELECT current_setting('block_size')::NUMERIC) AS bs,\n" +
            "                          CASE WHEN SUBSTRING(v,12,3) IN ('8.0','8.1','8.2') THEN 27 ELSE 23 END AS hdr,\n" +
            "                          CASE WHEN v ~ 'mingw32' THEN 8 ELSE 4 END AS ma,\n" +
            "                          v\n" +
            "                      FROM (SELECT version() AS v) AS foo\n" +
            "                  ) AS constants\n" +
            "                  GROUP BY 1,2,3,4,5\n" +
            "              ) AS foo\n" +
            "     ) AS rs\n" +
            "         JOIN pg_stat_user_tables pst ON rs.tablename = pst.relname\n" +
            "         JOIN pg_class cc ON cc.oid = pst.relid\n" +
            "         JOIN pg_namespace nn ON cc.relnamespace = nn.oid\n" +
            "    AND nn.nspname = rs.schemaname\n" +
            "    AND nn.nspname <> 'information_schema';\n")
    ArrayList<Object[]> executeMetricsQuery();
}
