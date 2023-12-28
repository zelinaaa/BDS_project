package org.but.feec.ars.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.but.feec.ars.App;
import org.but.feec.ars.api.AddressDetailView;
import org.but.feec.ars.config.DataSourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddressRepository {

    private static final Logger logger = LoggerFactory.getLogger(AddressRepository.class);

    public ObservableList<AddressDetailView> getAllAddresses(){
        List<AddressDetailView> addressList = new ArrayList<>();
        //String selectAllAddress = "select address_id, country_id, city_id, street, building_number, postal_code from bds.address";
        String selectAllAddress = "select a.address_id, a.country_id, a.city_id, a.street, a.building_number, a.postal_code, co.country, c.city from bds.address a join bds.city c" +
                " on a.city_id=c.city_id join bds.country co on a.country_id=co.country_id";

        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                selectAllAddress
             )){
            try (ResultSet rs = ps.executeQuery()){
                while (rs.next()){
                    addressList.add(mapToAddress(rs));
                }
            }
        }catch (SQLException e){
            System.out.println(e.toString());
            logger.error("Select all address from database failed: " + e.getMessage());
        }
        logger.info("Select all address from database successful.");
        return FXCollections.observableArrayList(addressList);
    }

    private AddressDetailView mapToAddress(ResultSet rs) throws SQLException{
        AddressDetailView addressView = new AddressDetailView();
        addressView.setAddress_id(rs.getInt("address_id"));
        addressView.setCountry_id(rs.getInt("country_id"));
        addressView.setCity_id(rs.getInt("city_id"));
        addressView.setStreet(rs.getString("street"));
        addressView.setBuilding_number(rs.getString("building_number"));
        addressView.setPostal_code(rs.getString("postal_code"));
        addressView.setCity(rs.getString("city"));
        addressView.setCountry(rs.getString("country"));

        return addressView;
    }

}
