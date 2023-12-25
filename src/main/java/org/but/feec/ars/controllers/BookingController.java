package org.but.feec.ars.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.but.feec.ars.App;
import org.but.feec.ars.api.*;
import org.but.feec.ars.data.BookingRepository;
import org.but.feec.ars.data.CustomerRepository;
import org.but.feec.ars.data.FlightRepository;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

public class BookingController {

    @FXML private Label balanceLabel;
    @FXML private Label priceLabel;
    @FXML private ComboBox classComboBox;
    @FXML private ComboBox seatComboBox;
    @FXML private Button confirmButton;
    @FXML private Button cancelButton;

    private SearchBookingController parentController;
    private FlightScheduleView selectedFlight;
    private AircraftFareView selectedFlightFare;
    ObservableList<AircraftFareView> aircraft;
    private CustomerCreateView loggedUserOnlyEmail;
    private CustomerRepository customerRepository;
    private FlightRepository flightRepository;
    private BookingRepository bookingRepository;

    ObservableList<Integer> classes_id = FXCollections.observableArrayList();
    ObservableList<String> classes_name = FXCollections.observableArrayList();
    ObservableList<SeatView> seats;
    ObservableList<SeatView> seatsTemp = FXCollections.observableArrayList();
    ObservableList<BookingView> bookedSeats;

    private CustomerCreateView loggedUserWithInfo;

    public void initData(CustomerCreateView loggedUser, ObservableList<AircraftFareView> aircraft , FlightScheduleView flightScheduleView, SearchBookingController parentController){
        this.parentController = parentController;
        this.selectedFlight = flightScheduleView;
        this.aircraft = aircraft;
        this.loggedUserOnlyEmail = loggedUser;
        this.customerRepository = new CustomerRepository();
        this.flightRepository = new FlightRepository();
        this.bookingRepository = new BookingRepository();
        loggedUserWithInfo = customerRepository.getCustomerInfoByEmail(loggedUser.getEmail());

        //for aircraft_model_id
        AircraftFareView firstAircraft = aircraft.get(0);

        for (AircraftFareView aircraftFareView : aircraft){
            classes_id.add(aircraftFareView.getTravel_class_id());
        }

        HashMap<Integer, String> classes = new HashMap<Integer, String>();
        classes.put(1, "economy");
        classes.put(2, "economy+");
        classes.put(3, "business");
        classes.put(4, "first_class");

        for (Integer a : classes_id){
            classes_name.add(classes.get(a));
        }

        seats = flightRepository.getAircraftSeats(firstAircraft.getAircraft_model_id());
        bookedSeats = bookingRepository.getSearchedBookings(flightScheduleView.getFlight_id());

        classComboBox.setItems(classes_name);
        balanceLabel.setText(String.valueOf(loggedUserWithInfo.getBalance()));


    }

    public void handleConfirm(ActionEvent event) {
        ValidationSupport validation = new ValidationSupport();
        validation.registerValidator(classComboBox, Validator.createEmptyValidator("Select class!"));
        validation.registerValidator(seatComboBox, Validator.createEmptyValidator("Select seat!"));
        confirmButton.disableProperty().bind(validation.invalidProperty());

        if (checkIfEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Booking failed.");
            alert.setHeaderText("Booking failed. Enter all required parameters.");
            alert.showAndWait();
        }else {
            BookingView bookingView = new BookingView();
            bookingView.setPerson_id(loggedUserWithInfo.getPerson_id());
            bookingView.setFlight_id(selectedFlight.getFlight_id());

            for (SeatView seatView : seats){
                if (seatView.getSeat_number() == Integer.valueOf(seatComboBox.getValue().toString())){
                    bookingView.setSeat_id(seatView.getSeat_id());
                    break;
                }
            }

            bookingRepository.insertBooking(bookingView);

            Stage stageOld = (Stage) confirmButton.getScene().getWindow();
            stageOld.close();
        }

    }

    private boolean checkIfEmpty(){
        return seatComboBox.getValue() == null || seatComboBox.getValue().toString().isEmpty();
    }

    public void handleCancel(ActionEvent event) {
        Stage stageOld = (Stage) cancelButton.getScene().getWindow();
        stageOld.close();
    }

    public void handleClassComboBox(ActionEvent event) {
        HashMap<String, Integer> classes_id = new HashMap<String, Integer>();
        classes_id.put("economy", 1);
        classes_id.put("economy+", 2);
        classes_id.put("business", 3);
        classes_id.put("first_class", 4);

        String selectedValue = (String) classComboBox.getValue();
        Integer class_id = classes_id.get(selectedValue);

        for (AircraftFareView aircraftFareView : aircraft){
            if (aircraftFareView.getTravel_class_id() == class_id){
                Double farePrice = aircraftFareView.getFare_per_unit() * aircraftFareView.getTravel_class_fare_multiplier();
                BigDecimal bd = BigDecimal.valueOf(farePrice);
                bd = bd.setScale(2, RoundingMode.HALF_UP);
                priceLabel.setText(String.valueOf(bd));
                break;
            }else {
                priceLabel.setText("This model doesn't have this class available.");
            }
        }

        setSeats(class_id);

    }

    private void setSeats(Integer class_id){
        Integer a = 0;
        seatsTemp.clear();
        for (SeatView seatView : seats){
            if (seatView.getTravel_class_id() == class_id){
                for (BookingView bookingView : bookedSeats){
                    if (bookingView.getSeat_id() == seatView.getSeat_id()){
                        a = 1;
                        break;
                    }
                }
                if (a == 0){
                    seatsTemp.add(seatView);
                }else {
                    a = 0;
                }
            }
        }

        seatComboBox.setItems(seatsTemp);


    }
}
