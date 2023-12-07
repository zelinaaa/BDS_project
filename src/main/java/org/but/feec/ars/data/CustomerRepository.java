package org.but.feec.ars.data;

import org.but.feec.ars.api.CustomerAuthView;
import org.but.feec.ars.api.CustomerCreateView;
import org.but.feec.ars.config.DataSourceConfig;

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
}
