package com.example.task7.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariConfig;

import java.sql.Connection;
import java.sql.SQLException;


@Configuration
public class ConnectionPoolExample {

    private HikariDataSource dataSource;
    public ConnectionPoolExample() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/task7");
        config.setUsername("root");
        config.setPassword("1234");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setMaximumPoolSize(3);
        config.setMinimumIdle(2);
        dataSource = new HikariDataSource(config);
    }

    @Bean
    public HikariDataSource dataSource() {
        return dataSource;
    }
    int con=1;

    public Connection getConnection() throws SQLException {
        System.out.println("+ Num of Idle Connections:: " + dataSource.getMinimumIdle());
        System.out.println("+ Num of Busy Connections: " + dataSource.getMaximumPoolSize());
        return dataSource.getConnection();
    }
}


