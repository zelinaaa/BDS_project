package org.but.feec.ars.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.but.feec.ars.api.BookingView;
import org.but.feec.ars.api.SeatView;
import org.but.feec.ars.config.DataSourceConfig;
import org.but.feec.ars.controllers.BookingController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingRepository {

    private static final Logger logger = LoggerFactory.getLogger(BookingRepository.class);

    public ObservableList<BookingView> getSearchedBookings(Integer flight_id){
        String searchBookings = "select booking_id, booking_status, booking_made, seat_id, person_id from bds.booking where flight_id = ?";

        List<BookingView> bookingViewList = new ArrayList<>();

        try (Connection connection = DataSourceConfig.getConnection()){
            PreparedStatement ps = connection.prepareStatement(searchBookings);
            ps.setInt(1, flight_id);
            try (ResultSet rs = ps.executeQuery()){
                while (rs.next()){
                    if (rs.getString("booking_status").equals("cancelled") == false){
                        bookingViewList.add(mapToBooking(rs));
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Select searched bookings from database failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
        logger.info("Select searched bookings from database successful.");
        return FXCollections.observableArrayList(bookingViewList);
    }

    public ObservableList<BookingView> getSearchedBookingsByPerson(Integer person_id){
        String searchBookings = "select booking_id, booking_status, booking_made, seat_id, flight_id from bds.booking where person_id = ?";

        List<BookingView> bookingViewList = new ArrayList<>();

        try (Connection connection = DataSourceConfig.getConnection()){
            PreparedStatement ps = connection.prepareStatement(searchBookings);
            ps.setInt(1, person_id);
            try (ResultSet rs = ps.executeQuery()){
                while (rs.next()){
                    bookingViewList.add(mapToBookingPersonID(rs, person_id));
                }
            }
        } catch (SQLException e) {
            logger.error("Select searched bookings from database failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
        logger.info("Select searched bookings from database successful.");
        return FXCollections.observableArrayList(bookingViewList);
    }

    public void updateBookingStatus(Integer booking_id, String status){
        String updateStatus = "update bds.booking b set booking_status=?::bds.booking_status_select where b.booking_id=?";
        Connection connection = null;

        try{
            connection = DataSourceConfig.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement(updateStatus);
            ps.setString(1, status);
            ps.setLong(2, booking_id);

            ps.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            try {
                logger.error("Updating booking failed, rolling back transaction: " + e.getMessage());
                connection.rollback();
            } catch (SQLException ex) {
                logger.error("Rolling back transaction in updating booking failed: " + ex.getMessage());
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
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
                logger.error("Creating booking in database failed.");
                throw new RuntimeException("Creating booking failed.");
            }

        } catch (SQLException e) {
            logger.error("Creating booking in database failed.");
            throw new RuntimeException(e);
        }
        logger.info("Creating booking in database successful.");
    }

    public SeatView getSeatInfoBySeatID(Integer seat_id){
        String getSeatInfo = "select seat_number, aircraft_model_id, travel_class_id from bds.seat where seat_id = ?";
        Connection connection = null;

        try{
            connection = DataSourceConfig.getConnection();
            PreparedStatement ps = connection.prepareStatement(getSeatInfo);
            ps.setInt(1, seat_id);

            try (ResultSet rs = ps.executeQuery()){
                if (rs.next()){
                    SeatView seatView = new SeatView();
                    seatView.setSeat_number(rs.getInt("seat_number"));
                    seatView.setTravel_class_id(rs.getInt("travel_class_id"));
                    seatView.setAircraft_model_id(rs.getInt("aircraft_model_id"));
                    return seatView;
                }
            }

        } catch (SQLException e) {
            logger.error("Getting info by seat id in database failed.");
            throw new RuntimeException(e);
        }
        return null;
    }

    public void insertBookingPayment(Integer booking_id, Double amount, String status){
        String insertPayment = "insert into bds.booking_payment (booking_id, amount, payment_method, payment_status)" +
                " values (?, ?, ?::bds.payment_method_select, ?::bds.payment_status_select)";

        Connection connection = null;
        try {
            connection = DataSourceConfig.getConnection();
            PreparedStatement ps = connection.prepareStatement(insertPayment);
            ps.setLong(1, booking_id);
            ps.setDouble(2, amount);
            ps.setString(3, "payment card");
            ps.setString(4, status);

            int affectedRows = ps.executeUpdate();
            System.out.println(affectedRows);

            if (affectedRows == 0){
                logger.error("Creating booking payment in database failed.");
                throw new RuntimeException("Creating booking payment failed.");
            }

        } catch (SQLException e) {
            logger.error("Creating booking payment in database failed: "+ e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void removeBooking(Integer booking_id){
        String delete = "delete from bds.booking where booking_id =?";
        Connection connection = null;

        try{
            connection = DataSourceConfig.getConnection();
            PreparedStatement ps = connection.prepareStatement(delete);
            ps.setInt(1, booking_id);
            ps.executeUpdate();

        } catch (SQLException e) {
            logger.error("Deleting booking in database failed: "+ e.getMessage());
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

    private BookingView mapToBookingPersonID(ResultSet rs, Integer person_id) throws SQLException{
        BookingView bookingView = new BookingView();
        bookingView.setSeat_id(rs.getInt("seat_id"));
        bookingView.setFlight_id(rs.getInt("flight_id"));
        bookingView.setBooking_made(rs.getString("booking_made"));
        bookingView.setBooking_id(rs.getInt("booking_id"));
        bookingView.setBooking_status(rs.getString("booking_status"));
        bookingView.setPerson_id(person_id);

        return bookingView;
    }


}
