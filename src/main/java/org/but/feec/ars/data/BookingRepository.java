package org.but.feec.ars.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.but.feec.ars.api.BookingView;
import org.but.feec.ars.config.DataSourceConfig;
import org.but.feec.ars.controllers.BookingController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookingRepository {

    public ObservableList<BookingView> getSearchedBookings(Integer flight_id){
        String searchBookings = "select booking_id, booking_status, booking_made, seat_id, person_id from bds.booking where flight_id = ?";

        List<BookingView> bookingViewList = new ArrayList<>();

        try (Connection connection = DataSourceConfig.getConnection()){
            PreparedStatement ps = connection.prepareStatement(searchBookings);
            ps.setInt(1, flight_id);
            try (ResultSet rs = ps.executeQuery()){
                while (rs.next()){
                    bookingViewList.add(mapToBooking(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return FXCollections.observableArrayList(bookingViewList);
    }

    private BookingView mapToBooking(ResultSet rs) throws SQLException{
        BookingView bookingView = new BookingView();
        bookingView.setBooking_id(rs.getInt("booking_id"));
        bookingView.setBooking_made(rs.getString("booking_made"));
        bookingView.setBooking_status(rs.getString("booking_status"));
        bookingView.setPerson_id(rs.getInt("person_id"));
        bookingView.setSeat_id(rs.getInt("seat_id"));

        return bookingView;
    }


}
