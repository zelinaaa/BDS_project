package org.but.feec.ars.data;

import org.but.feec.ars.api.CustomerAuthView;
import org.but.feec.ars.api.CustomerCreateView;
import org.but.feec.ars.config.DataSourceConfig;

import java.io.PrintStream;
import java.sql.*;

public class CustomerRepository {

    public CustomerAuthView findCustomerByEmail(String email){
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                    "select email, password_hash from bds.person where email=?"
            )){
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()){
                if (rs.next()){
                    return mapToPersonAuth(rs);
                }
            }
        }
        catch (SQLException e){
            System.out.println(e.toString());
        }
        return null;
    }

   public CustomerCreateView getCustomerInfoByEmail(String email){
        String selectAll = "select person_id, first_name, family_name, date_of_birth, gender, phone, address_id, balance, bookings_created, password_hash" +
                " from bds.person where email=?";

        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     selectAll
             )){
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()){
                if (rs.next()){
                    return mapToCustomerView(rs);
                }
            }
        }
        catch (SQLException e){
            System.out.println(e.toString());
        }
        return null;
   }

    public void createCustomer(CustomerCreateView customerCreateView){
        String insertCustomer = "insert into bds.person (email, password_hash) values (?,?)";

        try (Connection connection = DataSourceConfig.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insertCustomer, Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString(1, customerCreateView.getEmail());
            preparedStatement.setString(2, String.valueOf(customerCreateView.getPassword()));

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0){
                //dodělat throw
            }

        } catch (SQLException e) {
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
        customerCreateView.setBalance(rs.getInt("balance"));
        customerCreateView.setBookings_created(rs.getInt("bookings_created"));
        customerCreateView.setPassword(rs.getString("password_hash").toCharArray());

        return customerCreateView;
    }
}
