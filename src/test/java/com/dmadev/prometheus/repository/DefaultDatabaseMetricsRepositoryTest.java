package com.dmadev.prometheus.repository;

import com.dmadev.prometheus.api.response.DatabaseMetricResult;
import com.dmadev.prometheus.repository.DefaultDatabaseMetricsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


class DefaultDatabaseMetricsRepositoryTest {
    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private Resource queryResource;

    @InjectMocks
    private DefaultDatabaseMetricsRepository defaultDatabaseMetricsRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void executeMetricsQuery_returnMetrics() throws IOException {
        // Given
        String sqlQuery = "SELECT rs.schemaname, tablename, cc.reltuples, cc.relpages, bs, "
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


        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(queryResource.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        when(queryResource.getInputStream()).thenReturn(new ByteArrayInputStream(sqlQuery.getBytes()));

        List<DatabaseMetricResult> mockResults = new ArrayList<>();
        DatabaseMetricResult result = new DatabaseMetricResult();
        result.setSchemaname("public");
        result.setTablename("employee");
        result.setReltuples(1000L);
        result.setRelpages(100L);
        result.setFile_size_b(2048.0f);
        result.setData_size_b(1024.0f);
        mockResults.add(result);

        when(jdbcTemplate.query(anyString(), any(BeanPropertyRowMapper.class)))
                .thenReturn(mockResults);

        // When
        List<DatabaseMetricResult> results = defaultDatabaseMetricsRepository.
                executeMetricsQuery();

        // Then
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("public", results.get(0).getSchemaname());
        assertEquals("employee", results.get(0).getTablename());
        assertEquals(1000, results.get(0).getReltuples());
        assertEquals(100, results.get(0).getRelpages());
        assertEquals(2048.0f, results.get(0).getFile_size_b());
        assertEquals(1024.0f, results.get(0).getData_size_b());

    }
}

