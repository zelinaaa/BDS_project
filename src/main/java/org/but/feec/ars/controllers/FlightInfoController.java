package org.but.feec.ars.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import org.but.feec.ars.api.AircraftFareView;
import org.but.feec.ars.api.FlightInfoView;
import org.but.feec.ars.data.FlightRepository;

import java.util.HashMap;

public class FlightInfoController {

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

    public void initData(FlightInfoView selectedFlight, SearchBookingController parentController){
        this.selectedFlight = selectedFlight;
        this.parentController = parentController;
        this.flightRepository = new FlightRepository();

        classes = FXCollections.observableArrayList();
        classes.addAll("economy", "economy+", "business", "first class");
        fareComboBox.setItems(classes);

        aircraft = flightRepository.getAircraftInfo(selectedFlight.getFlight_id());

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

        //System.out.println(selectedFlight.getFlight_id());

    }


    public void handleComboBox(ActionEvent event) {
        HashMap<String, Integer> classes_id = new HashMap<String, Integer>();
        classes_id.put("economy", 1);
        classes_id.put("economy+", 2);
        classes_id.put("business", 3);
        classes_id.put("first_class", 4);

        String selectedValue = (String) fareComboBox.getValue();
        Integer class_id = classes_id.get(selectedValue);

        for (AircraftFareView aircraftFareView : aircraft){
            if (aircraftFareView.getTravel_class_id() == class_id){
                Double farePrice = aircraftFareView.getFare_per_unit() * aircraftFareView.getTravel_class_fare_multiplier();
                fareLabel.setText(String.valueOf(farePrice));
                break;
            }else {
                fareLabel.setText("This model doesn't have this class available.");
            }
        }

        //System.out.println("he");
    }
}
