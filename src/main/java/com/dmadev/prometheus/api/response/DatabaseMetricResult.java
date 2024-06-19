package com.dmadev.prometheus.api.response;

import lombok.Data;

@Data
public final class DatabaseMetricResult {
    private String schemaname;
    private String tablename;
    private Long reltuples;
    private Long relpages;
    private Integer bs;
    private Float file_size_b;
    private Float data_size_b;
}
