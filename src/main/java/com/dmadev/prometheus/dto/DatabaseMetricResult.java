package com.dmadev.prometheus.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.concurrent.atomic.AtomicLong;

@Data
public final class DatabaseMetricResult {
    @NotNull
    private String schemaname;
    @NotNull
    private String tablename;
    @NotNull
    private Long rowCount;

}

