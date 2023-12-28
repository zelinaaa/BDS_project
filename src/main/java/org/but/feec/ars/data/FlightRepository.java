package org.but.feec.ars.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.but.feec.ars.api.AircraftFareView;
import org.but.feec.ars.api.FlightInfoView;
import org.but.feec.ars.api.FlightScheduleView;
import org.but.feec.ars.api.SeatView;
import org.but.feec.ars.config.DataSourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FlightRepository {

    private static final Logger logger = LoggerFactory.getLogger(FlightRepository.class);


    public ObservableList<FlightScheduleView> getSearchedFlights(String searched){
        String selectAirportByName = "select airport_id from bds.airport where name=?";
        String selectAllFlights = "select fs.flight_id, origin.name as origin_airport_name, destination.name as destination_airport_name,\n" +
                "fs.departure_dt, fs.arrival_dt, fs.origin_airport_id, fs.destination_airport_id from bds.flight_schedule fs left join bds.airport origin on fs.origin_airport_id=origin.airport_id\n" +
                "left join bds.airport destination on fs.destination_airport_id=destination.airport_id\n" +
                "where fs.origin_airport_id=?";

        Integer selectID = null;
        List<FlightScheduleView> flightScheduleViewList = new ArrayList<>();

        try (Connection connection = DataSourceConfig.getConnection()){

            selectID = firstQuery(connection, selectAirportByName, searched);

            if (selectID == null){
                logger.info("Select id of airport_id is null. Returning null.");
                return FXCollections.observableArrayList(flightScheduleViewList);
            }

            PreparedStatement ps = connection.prepareStatement(selectAllFlights);

            ps.setInt(1, selectID);
            try (ResultSet rs = ps.executeQuery()){
                while (rs.next()){
                    flightScheduleViewList.add(mapToFlight(rs));
                }
            }catch (SQLException e){
                logger.error("Select searched flight failed: " + e.getMessage());
            }

        }catch (SQLException ex){
            logger.error("Select searched flight failed: " + ex.getMessage());
        }
        logger.info("Select searched flight successful.");
        return FXCollections.observableArrayList(flightScheduleViewList);
    }
    public ObservableList<FlightScheduleView> getAllFlights() {
        String selectAllFlights = "select fs.flight_id, origin.name as origin_airport_name, destination.name as destination_airport_name,\n" +
                "fs.departure_dt, fs.arrival_dt, fs.origin_airport_id, fs.destination_airport_id, fs.is_scheduled, fs.aircraft_id from bds.flight_schedule fs left join bds.airport origin on fs.origin_airport_id=origin.airport_id\n" +
                "left join bds.airport destination on fs.destination_airport_id=destination.airport_id";

        List<FlightScheduleView> flightScheduleViewList = new ArrayList<>();

        try (Connection connection = DataSourceConfig.getConnection()){
            PreparedStatement ps = connection.prepareStatement(selectAllFlights);
            try (ResultSet rs = ps.executeQuery()){
                while (rs.next()){
                    flightScheduleViewList.add(mapToFlight(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Select all flights failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
        logger.info("Select all flights successful.");
        return FXCollections.observableArrayList(flightScheduleViewList);
    }

    public ObservableList<AircraftFareView> getAircraftInfo(Integer aircraft_id){
        String selectAircraftInfo = "select am.model, am.fare_per_unit, amtcp.travel_class_fare_multiplier, amtcp.travel_class_id, a.aircraft_model_id " +
                "from bds.aircraft a left join bds.aircraft_model am on \n" +
                "a.aircraft_model_id=am.aircraft_model_id left join bds.aircraft_model_travel_class_pricing amtcp\n" +
                "on am.aircraft_model_id=amtcp.aircraft_model_id where aircraft_id = ?";

        List<AircraftFareView> aircraftFareViewList = new ArrayList<>();

        try (Connection connection = DataSourceConfig.getConnection()){
            PreparedStatement ps = connection.prepareStatement(selectAircraftInfo);
            ps.setInt(1, aircraft_id);
            try (ResultSet rs = ps.executeQuery()){
                while (rs.next()){
                    aircraftFareViewList.add(mapToAircraft(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Select aircraft info failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
        logger.info("Select aircraft info successful.");
        return FXCollections.observableArrayList(aircraftFareViewList);
    }

    public ObservableList<SeatView> getAircraftSeats(Integer aircraft_model_id){
        String selectAircraftSeats = "select seat_id, seat_number, travel_class_id from bds.seat where aircraft_model_id = ?";

        List<SeatView> seatViews = new ArrayList<>();

        try (Connection connection = DataSourceConfig.getConnection()){
            PreparedStatement ps = connection.prepareStatement(selectAircraftSeats);
            ps.setInt(1, aircraft_model_id);
            try (ResultSet rs = ps.executeQuery()){
                while (rs.next()){
                    seatViews.add(mapToSeat(rs));
                }
            }
        } catch (SQLException e){
            logger.error("Select aircraft seats failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
        logger.info("Select aircraft seats successful.");
        return FXCollections.observableArrayList(seatViews);
    }

    public FlightScheduleView getFlightInfo(Integer flight_id){
        String selectFlight = "select origin.name as origin_airport_name, destination.name as destination_airport_name,\n" +
                "fs.departure_dt, fs.arrival_dt, fs.origin_airport_id, fs.destination_airport_id, fs.aircraft_id from bds.flight_schedule\n" +
                "fs left join bds.airport origin on fs.origin_airport_id=origin.airport_id\n" +
                "left join bds.airport destination on fs.destination_airport_id=destination.airport_id\n" +
                "where fs.flight_id=?";
        Connection connection = null;

        try {
            connection = DataSourceConfig.getConnection();
            PreparedStatement ps = connection.prepareStatement(selectFlight);
            ps.setInt(1, flight_id);
            try (ResultSet rs = ps.executeQuery()){
                if (rs.next()){
                    FlightScheduleView flightScheduleView = new FlightScheduleView();
                    flightScheduleView.setAircraft_id(rs.getInt("aircraft_id"));
                    flightScheduleView.setOrigin_airport(rs.getString("origin_airport_name"));
                    flightScheduleView.setDeparture_dt(rs.getString("departure_dt"));
                    flightScheduleView.setArrival_dt(rs.getString("arrival_dt"));
                    flightScheduleView.setDestination_airport_id(rs.getInt("destination_airport_id"));
                    flightScheduleView.setOrigin_airport_id(rs.getInt("origin_airport_id"));
                    flightScheduleView.setDestination_airport(rs.getString("destination_airport_name"));

                    return flightScheduleView;

                }
            }
        } catch (SQLException e) {
            logger.error("Select aircraft seats failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    public AircraftFareView getFlighInfoBooking (Integer seat_id, Integer travel_class_id){
        String selectedBooking = "select am.model, am.fare_per_unit, amtcp.travel_class_fare_multiplier from bds.aircraft_model am left join\n" +
                "bds.aircraft_model_travel_class_pricing amtcp on am.aircraft_model_id=amtcp.aircraft_model_id left join\n" +
                "bds.seat s on s.aircraft_model_id=am.aircraft_model_id\n" +
                "where seat_id=? and amtcp.travel_class_id=?";
        Connection connection = null;

        try {
            connection = DataSourceConfig.getConnection();
            PreparedStatement ps = connection.prepareStatement(selectedBooking);
            ps.setInt(1, seat_id);
            ps.setInt(2, travel_class_id);
            try (ResultSet rs = ps.executeQuery()){
                if (rs.next()){
                    AircraftFareView aircraftFareView = new AircraftFareView();
                    aircraftFareView.setModel(rs.getString("model"));
                    aircraftFareView.setFare_per_unit(rs.getDouble("fare_per_unit"));
                    aircraftFareView.setTravel_class_fare_multiplier(rs.getDouble("travel_class_fare_multiplier"));

                    return aircraftFareView;
                }
            }

        } catch (SQLException e) {
            logger.error("Select aircraft seats failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    private SeatView mapToSeat(ResultSet rs) throws SQLException{
        SeatView seatView = new SeatView();
        seatView.setSeat_id(rs.getInt("seat_id"));
        seatView.setSeat_number(rs.getInt("seat_number"));
        seatView.setTravel_class_id(rs.getInt("travel_class_id"));

        return seatView;
    }

    private AircraftFareView mapToAircraft(ResultSet rs) throws SQLException{
        AircraftFareView aircraftFareView = new AircraftFareView();
        aircraftFareView.setModel(rs.getString("model"));
        aircraftFareView.setFare_per_unit(rs.getDouble("fare_per_unit"));
        aircraftFareView.setTravel_class_fare_multiplier(rs.getDouble("travel_class_fare_multiplier"));
        aircraftFareView.setTravel_class_id(rs.getInt("travel_class_id"));
        aircraftFareView.setAircraft_model_id(rs.getInt("aircraft_model_id"));

        return aircraftFareView;
    }

    private FlightScheduleView mapToFlight(ResultSet rs) throws SQLException{
        FlightScheduleView flightScheduleView = new FlightScheduleView();
        flightScheduleView.setFlight_id(rs.getInt("flight_id"));
        flightScheduleView.setArrival_dt(rs.getString("arrival_dt"));
        flightScheduleView.setDeparture_dt(rs.getString("departure_dt"));
        flightScheduleView.setDestination_airport(rs.getString("destination_airport_name"));
        flightScheduleView.setOrigin_airport(rs.getString("origin_airport_name"));
        flightScheduleView.setDestination_airport_id(rs.getInt("destination_airport_id"));
        flightScheduleView.setOrigin_airport_id(rs.getInt("origin_airport_id"));
        flightScheduleView.setIs_scheduled(rs.getString("is_scheduled"));
        flightScheduleView.setAircraft_id(rs.getInt("aircraft_id"));


        return flightScheduleView;
    }

    private Integer firstQuery(Connection connection, String selectAirportByName, String searched) throws SQLException{
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectAirportByName)){
            preparedStatement.setString(1, searched);
            try (ResultSet rs = preparedStatement.executeQuery()){
                if (rs.next()){
                    return rs.getInt("airport_id");
                }
            }
        }
        return null;
    }

}
