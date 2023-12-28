package org.but.feec.ars.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import org.but.feec.ars.api.AircraftFareView;
import org.but.feec.ars.api.CustomerCreateView;
import org.but.feec.ars.api.FlightInfoView;
import org.but.feec.ars.data.CustomerRepository;
import org.but.feec.ars.data.FlightRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

public class FlightInfoController {

    private static final Logger logger = LoggerFactory.getLogger(FlightInfoController.class);

    @FXML private Label originAirportLabel;
    @FXML private Label destinationAirportLabel;
    @FXML private Label aircraftModelLabel;
    @FXML private Label departureDatetimeLabel;
    @FXML private Label arrivalDatetimeLabel;
    @FXML private Label isScheduledLabel;
    @FXML private Label fareLabel;
    @FXML private ComboBox fareComboBox;

    private FlightInfoView selectedFlight;
    private SearchBookingController parentController;

    ObservableList<String> classes;
    ObservableList<AircraftFareView> aircraft;

    private FlightRepository flightRepository;
    private CustomerCreateView loggedUser;
    private CustomerRepository customerRepository;
    private CustomerCreateView loggedUserWithInfo;

    public void initData(CustomerCreateView loggedUser, FlightInfoView selectedFlight, SearchBookingController parentController){
        this.selectedFlight = selectedFlight;
        this.parentController = parentController;
        this.flightRepository = new FlightRepository();
        this.customerRepository = new CustomerRepository();
        this.loggedUser = loggedUser;

        loggedUserWithInfo = customerRepository.getCustomerInfoByEmail(loggedUser.getEmail());

        classes = FXCollections.observableArrayList();
        classes.addAll("economy", "economy+", "business", "first class");
        fareComboBox.setItems(classes);

        aircraft = flightRepository.getAircraftInfo(selectedFlight.getAircraft_id());

        originAirportLabel.setText(selectedFlight.getOrigin_airport());
        destinationAirportLabel.setText(selectedFlight.getDestination_airport());
        departureDatetimeLabel.setText(selectedFlight.getDeparture_time());
        arrivalDatetimeLabel.setText(selectedFlight.getArrival_time());

        if (selectedFlight.isIs_scheduled() == "t"){
            isScheduledLabel.setText("Yes");
        }else {
            isScheduledLabel.setText("No");
        }

        AircraftFareView firstAircraft = aircraft.get(0);
        aircraftModelLabel.setText(firstAircraft.getModel());

        logger.info(String.format("User %d selected information about flight with ID %d", loggedUser.getPerson_id(),selectedFlight.getFlight_id()));
    }


    public void handleComboBox(ActionEvent event) {
        HashMap<String, Integer> classes_id = new HashMap<String, Integer>();
        classes_id.put("economy", 1);
        classes_id.put("economy+", 2);
        classes_id.put("business", 3);
        classes_id.put("first class", 4);

        String selectedValue = (String) fareComboBox.getValue();
        Integer class_id = classes_id.get(selectedValue);

        for (AircraftFareView aircraftFareView : aircraft){
            if (aircraftFareView.getTravel_class_id() == class_id){
                Double farePrice = aircraftFareView.getFare_per_unit() * aircraftFareView.getTravel_class_fare_multiplier();
                BigDecimal bd = BigDecimal.valueOf(farePrice);
                bd = bd.setScale(2, RoundingMode.HALF_UP);
                fareLabel.setText(String.valueOf(bd));
                break;
            }else {
                fareLabel.setText("This model doesn't have this class available.");
            }
        }
    }
}
