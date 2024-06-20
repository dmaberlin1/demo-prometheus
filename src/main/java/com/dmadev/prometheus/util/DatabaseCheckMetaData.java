package com.dmadev.prometheus.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


@Component
@Slf4j
public final class DatabaseCheckMetaData {

    public static void checkMetaData() {
        try (Connection connection = ConnectionManager.get()) {
            var metaData = connection.getMetaData();
            log.info("Connected to database: {}", metaData.getDatabaseProductName());
            try (ResultSet catalogs = metaData.getCatalogs()) {
                while (catalogs.next()) {
                    String catalog = catalogs.getString(1);
                    log.info("Catalog: {}", catalog);

                    try (ResultSet schemas = metaData.getSchemas(catalog, null)) {
                        while (schemas.next()) {
                            String schema = schemas.getString("TABLE_SCHEM");
                            log.info("  Schema: {}", schema);

                            if ("public".equals(schema)) {
                                try (ResultSet tables = metaData.getTables(catalog, schema, "%", new String[]{"TABLE"})) {
                                    while (tables.next()) {
                                        String tableName = tables.getString("TABLE_NAME");
                                        log.info("    Table: {}", tableName);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.warn("Exception while checking metadata: ", e);
        }
    }
}
