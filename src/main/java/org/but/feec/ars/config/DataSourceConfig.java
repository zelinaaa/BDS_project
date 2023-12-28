package org.but.feec.ars.config;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.but.feec.ars.data.BookingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

public class DataSourceConfig {
    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    private static final Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);

    public static void preInit(){
        InputStream inputStream = DataSourceConfig.class.getClassLoader().getResourceAsStream("application.properties");
        try {
            initializeDataSource(inputStream);
        } catch (IOException | NullPointerException | IllegalArgumentException e){
            logger.error("Configurating database failed: " + e.getMessage());
        }catch (Exception e) {
            logger.error("Connect to database failed: " + e.getMessage());
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
        //logger.info("Connection to database established.");
        return ds.getConnection();
    }

}
