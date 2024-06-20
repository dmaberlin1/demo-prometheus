package com.dmadev.prometheus.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public final class DatabaseMetricResult {
    @NotNull
    private String schemaname;
    @NotNull
    private String tablename;
    @NotNull
    private Long rowCount;
    @NotNull
    private String tableSize;
}

