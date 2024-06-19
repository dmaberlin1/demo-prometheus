package com.dmadev.prometheus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.DriverManager;

@Configuration
@ComponentScan("com.dmadev.prometheus")
public class SpringJdbcConfig {

    @Bean
    @Primary
    public DataSource myPostgresDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/aia_perf1");
        dataSource.setUsername("postgres");
        dataSource.setPassword("Postgres01");
        return dataSource;
    }
}


