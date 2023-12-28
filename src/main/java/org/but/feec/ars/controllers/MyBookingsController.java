package org.but.feec.ars.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.but.feec.ars.api.*;
import org.but.feec.ars.data.BookingRepository;
import org.but.feec.ars.data.CustomerRepository;
import org.but.feec.ars.data.FlightRepository;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import javafx.beans.binding.Bindings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

public class MyBookingsController {

    private static final Logger logger = LoggerFactory.getLogger(MyBookingsController.class);

    @FXML private Label balanceLabel;
    @FXML private Label originAirportLabel;
    @FXML private Label destinationAirportTable;
    @FXML private Label aircraftModelLabel;
    @FXML private Label departureDatetimeLabel;
    @FXML private Label arrivalDatetimeTable;
    @FXML private Label priceLabel;
    @FXML private Label seatNumberLabel;
    @FXML private Label classLabel;
    @FXML private Button cancelButton;
    @FXML private Button payButton;
    @FXML private Button topUpButton;
    @FXML private ListView bookingsListView;
    @FXML private TextField topUpTextField;

    private CustomerCreateView loggedUser;
    private CustomerRepository customerRepository;
    private BookingRepository bookingRepository;
    private FlightRepository flightRepository;
    ObservableList<BookingView> bookings;

    public void initData(CustomerCreateView loggedUser, LoggedInController parentController){
        this.loggedUser = loggedUser;
        this.customerRepository = new CustomerRepository();
        this.bookingRepository = new BookingRepository();
        this.flightRepository = new FlightRepository();
        bookings = bookingRepository.getSearchedBookingsByPerson(loggedUser.getPerson_id());

        if (loggedUser.getBalance() == null){
            balanceLabel.setText("0");
        } else {
            balanceLabel.setText(String.valueOf(loggedUser.getBalance()));
        }

        bookingsListView.setItems(bookings);
    }

    public void handleTopUp(ActionEvent event) {
        logger.info(String.format("User %d tries to top up his account balance.", loggedUser.getPerson_id()));
        try {
            Double valueInTextField = Double.valueOf(topUpTextField.getText());
            if (valueInTextField <=0 ){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Top up failed!");
                alert.setHeaderText("Entered wrong, try again.");
                alert.showAndWait();
            }else {
                Double newBalance = loggedUser.getBalance() + valueInTextField;
                loggedUser.setBalance(newBalance);
                balanceLabel.setText(String.valueOf(loggedUser.getBalance()));
                customerRepository.updateBalance(loggedUser.getPerson_id(), newBalance);
                topUpTextField.setText("");
            }
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Top up failed!");
            alert.setHeaderText("Entered wrong, try again.");
            alert.showAndWait();
        }
    }

    public void handlePay(ActionEvent event) {
        try{
            Double difference = loggedUser.getBalance() - Double.valueOf(priceLabel.getText());
            BookingView bookingView = (BookingView) bookingsListView.getSelectionModel().getSelectedItem();

            logger.info(String.format("User %d tries to pay for booking %d.", loggedUser.getPerson_id(), bookingView.getBooking_id()));

            if (difference < 0){
                bookingRepository.insertBookingPayment(bookingView.getBooking_id(), Double.valueOf(priceLabel.getText()), "failed");
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Paying booking failed!");
                alert.setHeaderText("You don't have enough money..");
                alert.showAndWait();
            }else {
                bookingRepository.insertBookingPayment(bookingView.getBooking_id(), Double.valueOf(priceLabel.getText()), "successful");

                Double newBalance = difference;
                loggedUser.setBalance(newBalance);
                balanceLabel.setText(String.valueOf(loggedUser.getBalance()));
                customerRepository.updateBalance(loggedUser.getPerson_id(), newBalance);

                bookingRepository.updateBookingStatus(bookingView.getBooking_id(), "confirmed");

                updateListView();
            }
        }catch (Exception e){

        }
    }

    public void handleCancel(ActionEvent event) {
        try{
            BookingView bookingView = (BookingView) bookingsListView.getSelectionModel().getSelectedItem();

            logger.info(String.format("User %d tries to cancel booking %d.", loggedUser.getPerson_id(), bookingView.getBooking_id()));

            //bookingRepository.updateBookingStatus(bookingView.getBooking_id(), "canceled");
            bookingRepository.removeBooking(bookingView.getBooking_id());
            updateListView();

            originAirportLabel.setText("");
            destinationAirportTable.setText("");
            aircraftModelLabel.setText("");
            departureDatetimeLabel.setText("");
            arrivalDatetimeTable.setText("");
            seatNumberLabel.setText("");
            classLabel.setText("");
            priceLabel.setText("");

        } catch (Exception e){

        }
    }

    public void handleSelectBooking(MouseEvent mouseEvent) {
        try{
            BookingView bookingView = (BookingView) bookingsListView.getSelectionModel().getSelectedItem();

            if (bookingView.getBooking_status().equals("confirmed")){
                payButton.setDisable(true);
            } else {
                payButton.setDisable(false);
            }

            SeatView selectedBooking = bookingRepository.getSeatInfoBySeatID(bookingView.getSeat_id());
            selectedBooking.setSeat_id(bookingView.getSeat_id());

            FlightScheduleView aircraft = flightRepository.getFlightInfo(bookingView.getFlight_id());

            HashMap<Integer, String> classes = new HashMap<Integer, String>();
            classes.put(1, "economy");
            classes.put(2, "economy+");
            classes.put(3, "business");
            classes.put(4, "first class");

            classLabel.setText(classes.get(selectedBooking.getTravel_class_id()));
            originAirportLabel.setText(aircraft.getOrigin_airport());
            destinationAirportTable.setText(aircraft.getDestination_airport());
            departureDatetimeLabel.setText(aircraft.getDeparture_dt());
            arrivalDatetimeTable.setText(aircraft.getArrival_dt());
            seatNumberLabel.setText(String.valueOf(selectedBooking.getSeat_number()));

            AircraftFareView aircraftFareView = flightRepository.getFlighInfoBooking(bookingView.getSeat_id(), selectedBooking.getTravel_class_id());

            aircraftModelLabel.setText(aircraftFareView.getModel());

            Double farePrice = aircraftFareView.getFare_per_unit() * aircraftFareView.getTravel_class_fare_multiplier();
            BigDecimal bd = BigDecimal.valueOf(farePrice);
            bd = bd.setScale(2, RoundingMode.HALF_UP);
            priceLabel.setText(String.valueOf(bd));
        }catch (Exception e){

        }

    }

    private void updateListView(){
        bookings = bookingRepository.getSearchedBookingsByPerson(loggedUser.getPerson_id());
        bookingsListView.setItems(bookings);
    }
}
