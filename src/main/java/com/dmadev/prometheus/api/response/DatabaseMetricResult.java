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

//schemaname: Название схемы таблицы.
// tablename: Название таблицы.
//reltuples: Количество строк в таблице.
//relpages: Количество страниц в таблице.
//bs: Размер блока (block size) базы данных.
//file_size_b: Размер файла таблицы в байтах.
//data_size_b: Размер данных таблицы в байтах.