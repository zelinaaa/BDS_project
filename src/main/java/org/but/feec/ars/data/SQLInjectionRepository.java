package org.but.feec.ars.data;

import org.but.feec.ars.config.DataSourceConfig;
import org.but.feec.ars.controllers.LogInController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.crypto.Data;
import java.sql.*;

public class SQLInjectionRepository {

    private static final Logger logger = LoggerFactory.getLogger(SQLInjectionRepository.class);

    public void createTable(){
        String create = "create table if not exists bds.sqlinjection (\n" +
                "\tperson_id serial,\n" +
                "\tfirst_name varchar(20),\n" +
                "\tlast_name varchar(30)\n" +
                ")\n" +
                "\n";

        Connection connection = null;

        try {
            connection = DataSourceConfig.getConnection();
            PreparedStatement ps = connection.prepareStatement(create);
            ps.executeUpdate();
            logger.info("Create sql injection table successful.");
        } catch (SQLException e) {
            logger.error("Create table failed: " + e.getMessage());
            //throw new RuntimeException(e);
        }
    }

    public void insertData(){
        String insert1 = "insert INTO bds.sqlinjection (first_name, last_name) values (?, ?)";
        String insert2 = "insert INTO bds.sqlinjection (first_name, last_name) values (?, ?)";
        String insert3 = "insert into bds.sqlinjection (first_name, last_name) values (?, ?)";
        String insert4 = "insert into bds.sqlinjection (first_name, last_name) values (?, ?)";
        Connection connection = null;
        try {
            connection = DataSourceConfig.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement(insert1);
            ps.setString(1, "Petr");
            ps.setString(2, "Zelinka");
            ps.executeUpdate();
            PreparedStatement ps2 = connection.prepareStatement(insert2);
            ps2.setString(1, "Aleš");
            ps2.setString(2, "Novák");
            ps2.executeUpdate();
            PreparedStatement ps3 = connection.prepareStatement(insert3);
            ps3.setString(1, "David");
            ps3.setString(2, "Vysoký");
            ps3.executeUpdate();
            PreparedStatement ps4 = connection.prepareStatement(insert4);
            ps4.setString(1, "Kateřina");
            ps4.setString(2, "Tichá");
            ps4.executeUpdate();

            connection.commit();
            logger.info("Insert data for sql injection successful.");
        } catch (SQLException e) {
            try {
                logger.error("Create table failed: " + e.getMessage());
                connection.rollback();
            } catch (SQLException ex) {
                //throw new RuntimeException(ex);
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                //throw new RuntimeException(e);
            }
        }
    }

    public void retrieveData(){
        String oneequalsone = "select first_name, last_name from bds.sqlinjection where person_id = 1596547 OR 1=1";

        try (Connection connection = DataSourceConfig.getConnection()){
            PreparedStatement ps = connection.prepareStatement(oneequalsone);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                System.out.println(rs.getString("first_name") + " " + rs.getString("last_name"));
            }
        } catch (SQLException e) {
            //throw new RuntimeException(e);
        }
    }

    public void dropTable(){
        String maliciousQuery = "'; DROP TABLE sqlinjection; --";
        String malQuery = "select person_id, first_name, last_name FROM sqlinjection s WHERE s.first_name = '" + maliciousQuery + "'";

        try (Connection connection = DataSourceConfig.getConnection();
             Statement s = connection.createStatement();
             ResultSet rs = s.executeQuery(malQuery)) {

        } catch (SQLException e) {
        }
    }
}
