package org.but.feec.ars.config;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

public class DataSourceConfig {
    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;


    public static void preInit(){
        //InputStream inputStream = null;
        InputStream inputStream = DataSourceConfig.class.getClassLoader().getResourceAsStream("application.properties");
        try {
            initializeDataSource(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void initializeDataSource(InputStream inputStream) throws IOException {
        Properties properties = new Properties();
        properties.load(inputStream);
        config.setJdbcUrl(properties.getProperty("datasource.url"));
        config.setUsername(properties.getProperty("datasource.username"));
        config.setPassword(properties.getProperty("datasource.password"));
        ds = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

}
