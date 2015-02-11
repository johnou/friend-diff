package com.hellface.facebook.configuration;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

/**
 * {@link DatabaseConfig} configures a custom c3p0 {@link DataSource}.
 *
 * @author Johno Crawford (johno@hellface.com)
 */
@Configuration
@EnableConfigurationProperties(DataSourceProperties.class)
public class DatabaseConfig {

    @Autowired
    private DataSourceProperties properties;

    @Value("${datasource.initialPoolSize:3}")
    private int initialPoolSize;

    @Value("${datasource.minPoolSize:1}")
    private int minPoolSize;

    @Value("${datasource.maxPoolSize:30}")
    private int maxPoolSize;

    @Value("${datasource.acquireIncrement:3}")
    private int acquireIncrement;

    @Value("${datasource.idleConnectionTestPeriod:30}")
    private int idleConnectionTestPeriod;

    @Bean
    @ConfigurationProperties(prefix = DataSourceProperties.PREFIX)
    public DataSource dataSource() {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        try {
            dataSource.setDriverClass(this.properties.getDriverClassName());
        } catch (PropertyVetoException e) {
            throw new IllegalArgumentException("Driver class not in classpath?");
        }
        dataSource.setJdbcUrl(this.properties.getUrl());
        dataSource.setUser(this.properties.getUsername());
        dataSource.setPassword(this.properties.getPassword());
        dataSource.setInitialPoolSize(this.initialPoolSize);
        dataSource.setMinPoolSize(this.minPoolSize);
        dataSource.setMaxPoolSize(this.maxPoolSize);
        dataSource.setAcquireIncrement(this.acquireIncrement);
        dataSource.setIdleConnectionTestPeriod(this.idleConnectionTestPeriod);
        dataSource.setPreferredTestQuery("SELECT 1");
        dataSource.setTestConnectionOnCheckin(true);
        dataSource.setTestConnectionOnCheckout(false);
        return dataSource;
    }
}
