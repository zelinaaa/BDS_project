package org.but.feec.ars.data;

import org.but.feec.ars.api.CustomerAuthView;
import org.but.feec.ars.api.CustomerCreateView;
import org.but.feec.ars.config.DataSourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class CustomerRepository {

    private static final Logger logger = LoggerFactory.getLogger(CustomerRepository.class);

    public CustomerAuthView findCustomerByEmail(String email){
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                    "select email, password_hash from bds.person where email=?"
            )){
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()){
                if (rs.next()){
                    logger.info("Select user by email for login from database successful.");
                    return mapToPersonAuth(rs);
                }
            }
        }
        catch (SQLException e){
            logger.error("Select user by email for login from database failed: " + e.getMessage());
            System.out.println(e.toString());
        }
        logger.info("Select user by email for login from database is null.");
        return null;
    }

   public CustomerCreateView getCustomerInfoByEmail(String email){
        String selectAll = "select person_id, first_name, family_name, date_of_birth, gender, phone, address_id, balance, password_hash" +
                " from bds.person where email=?";

        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     selectAll
             )){
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()){
                if (rs.next()){
                    logger.info("Select all information about user by email from database successful.");
                    return mapToCustomerView(rs);
                }
            }
        }
        catch (SQLException e){
            logger.error("Select all information about user by email from database failed: " + e.getMessage());
            System.out.println(e.toString());
        }
        logger.info("Select all information about user by email from database is null.");
        return null;
   }

    public boolean createCustomer(CustomerCreateView customerCreateView){
        String insertCustomer = "insert into bds.person (email, password_hash) values (?,?)";
        String selectPersonID = "select person_id from bds.person where email=?";
        String assignCustomerRole = "insert into bds.person_has_role (person_id, role_id) values (?, ?)";

        try {
            Connection connection = DataSourceConfig.getConnection();
            try {
                connection.setAutoCommit(false);

                PreparedStatement ps1 = connection.prepareStatement(insertCustomer);
                ps1.setString(1, customerCreateView.getEmail());
                ps1.setString(2, String.valueOf(customerCreateView.getPassword()));

                int affectedRows = ps1.executeUpdate();

                if (affectedRows == 0) {
                    logger.error("Creating customer in database failed.");
                    throw new RuntimeException("Creating booking failed.");
                }

                Integer person_id = null;

                PreparedStatement ps2 = connection.prepareStatement(selectPersonID);
                ps2.setString(1, customerCreateView.getEmail());
                ResultSet rs = ps2.executeQuery();
                if (rs.next()) {
                    logger.error("Select customer from database successful.");
                    person_id = rs.getInt("person_id");
                }

                PreparedStatement ps3 = connection.prepareStatement(assignCustomerRole);
                ps3.setInt(1, person_id);
                ps3.setInt(2, 1);
                int affectedRows2 = ps3.executeUpdate();

                if (affectedRows2 == 0) {
                    logger.error("Assigning role to customer failed.");
                    throw new RuntimeException("Assigning role to customer failed.");
                }
                return true;
            } catch (SQLException e){
                logger.error(String.format("Error in creating customer, rollbacking connection.", e.getMessage()));
                connection.rollback();
                return false;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            logger.error("Creating customer failed.");
            throw new RuntimeException(e);
        }
    }

    public void updateCustomer(CustomerCreateView customerCreateView){
        String updateCustomer = "update bds.person p set first_name=?, family_name=?, date_of_birth=?, gender=?::bds.gender_select, phone=?, address_id=?" +
                " where p.person_id=?";

        Connection connection = null;

        try{
            connection = DataSourceConfig.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement(updateCustomer);
            ps.setString(1, customerCreateView.getFirst_name());
            ps.setString(2, customerCreateView.getFamily_name());
            ps.setDate(3, Date.valueOf(customerCreateView.getDate_of_birth()));
            ps.setString(4, customerCreateView.getGender());
            ps.setString(5, customerCreateView.getPhone());
            ps.setLong(6, customerCreateView.getAddress_id());
            ps.setLong(7, customerCreateView.getPerson_id());

            ps.executeUpdate();
            connection.commit();

        }catch (SQLException e){
            try{
                logger.error("Updating customer failed, rolling back transaction: " + e.getMessage());
                connection.rollback();
            }catch (Exception e2){
                logger.error("Rolling back transaction in updating customer failed: " + e2.getMessage());
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Integer getRoleID(String email){
        String selectPersonID = "select person_id from bds.person where email=?";
        String selectRoleID = "select role_id from bds.person_has_role where person_id=?";

        Integer person_id = null;

        Connection connection = null;

        try {
            connection = DataSourceConfig.getConnection();

            PreparedStatement ps1 = connection.prepareStatement(selectPersonID);
            ps1.setString(1, email);
            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next()){
                logger.info("Select person_id from database successful.");
                person_id = rs1.getInt("person_id");
            }

            PreparedStatement ps2 = connection.prepareStatement(selectRoleID);
            ps2.setInt(1, person_id);
            ResultSet rs2 = ps2.executeQuery();
            if (rs2.next()){
                logger.info("Select role of person login successful.");
                return rs2.getInt("role_id");
            }
        } catch (SQLException e) {
            logger.info("Select role of person failed: " + e.getMessage());
            System.out.println(e);
        }
        logger.info("Select role of person is null.");
        return null;
    }

    public void updateBalance(Integer person_id, Double balance){
        String updateBalance = "update bds.person p set balance = ? where person_id = ?";
        Connection connection = null;

        try{
            connection = DataSourceConfig.getConnection();
            PreparedStatement ps = connection.prepareStatement(updateBalance);
            ps.setDouble(1, balance);
            ps.setInt(2, person_id);
            ps.executeUpdate();

        } catch (SQLException e) {
            logger.info(String.format("Update balance of person %d failed: %s", person_id, e.getMessage()));
            throw new RuntimeException(e);
        }

    }


    private CustomerAuthView mapToPersonAuth(ResultSet rs) throws SQLException {
        CustomerAuthView customer = new CustomerAuthView();
        customer.setEmail(rs.getString("email"));
        customer.setPassword(rs.getString("password_hash"));
        return customer;
    }

    private CustomerCreateView mapToCustomerView(ResultSet rs) throws SQLException{
        CustomerCreateView customerCreateView = new CustomerCreateView();
        customerCreateView.setFirst_name(rs.getString("first_name"));
        customerCreateView.setFamily_name(rs.getString("family_name"));
        customerCreateView.setDate_of_birth(rs.getString("date_of_birth"));
        customerCreateView.setGender(rs.getString("gender"));
        customerCreateView.setPhone(rs.getString("phone"));
        customerCreateView.setAddress_id(rs.getInt("address_id"));
        customerCreateView.setPerson_id(rs.getInt("person_id"));
        customerCreateView.setBalance(rs.getDouble("balance"));
        customerCreateView.setPassword(rs.getString("password_hash").toCharArray());

        return customerCreateView;
    }
}
