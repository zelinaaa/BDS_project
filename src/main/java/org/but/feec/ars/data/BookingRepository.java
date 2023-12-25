package org.but.feec.ars.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.but.feec.ars.api.BookingView;
import org.but.feec.ars.config.DataSourceConfig;
import org.but.feec.ars.controllers.BookingController;

import java.sql.*;
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

    public void insertBooking(BookingView bookingView){
        String insertBooking = "insert into bds.booking (flight_id, person_id, seat_id) values (?, ?, ?)";

        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertBooking, Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setInt(1, bookingView.getFlight_id());
            preparedStatement.setInt(2, bookingView.getPerson_id());
            preparedStatement.setInt(3, bookingView.getSeat_id());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0){
                //dodělat throw
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

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
