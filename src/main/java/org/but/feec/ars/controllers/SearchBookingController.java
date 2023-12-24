package org.but.feec.ars.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.but.feec.ars.App;
import org.but.feec.ars.api.AircraftFareView;
import org.but.feec.ars.api.FlightInfoView;
import org.but.feec.ars.api.FlightScheduleView;
import org.but.feec.ars.data.FlightRepository;

import java.io.IOException;

public class SearchBookingController {

    @FXML private Button bookButton;
    @FXML private Button cancelButton;
    @FXML private ListView listView;

    private LoggedInController parentController;
    private FlightRepository flightRepository;

    ObservableList<FlightScheduleView> flights;
    //ObservableList<AircraftFareView> aircraft;

    public void initData(String searched, LoggedInController parentController){
        this.parentController = parentController;
        this.flightRepository = new FlightRepository();

        if (searched == ""){
            flights = flightRepository.getAllFlights();
        } else {
            flights = flightRepository.getSearchedFlights(searched);
        }

        listView.setItems(flights);

    }


    public void handleBook(ActionEvent event) {
    }

    public void handleCancel(ActionEvent event) {
    }

    public void handleMouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)){
            if (mouseEvent.getClickCount() == 2){

                FlightInfoView flightInfoView = new FlightInfoView();
                FlightScheduleView flightScheduleView = (FlightScheduleView) listView.getSelectionModel().getSelectedItem();

                flightInfoView.setFlight_id(flightScheduleView.getFlight_id());
                flightInfoView.setArrival_time(flightScheduleView.getArrival_dt());
                flightInfoView.setDeparture_time(flightScheduleView.getDeparture_dt());
                flightInfoView.setDestination_airport(flightScheduleView.getDestination_airport());
                flightInfoView.setOrigin_airport(flightScheduleView.getOrigin_airport());
                flightInfoView.setIs_scheduled(flightScheduleView.isIs_scheduled());


                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(App.class.getResource("fxml/FlightInfo.fxml"));
                Scene scene = null;
                try{
                    scene = new Scene(fxmlLoader.load());
                }catch (IOException e){
                    throw new RuntimeException(e);
                }
                Stage stage = new Stage();
                stage.setTitle("Search Bookings");
                stage.setScene(scene);

                FlightInfoController flightInfoController = fxmlLoader.getController();

                //aircraft = flightRepository.getAircraftInfo(flightScheduleView.getAircraft_id());
                //AircraftFareView aircraftFareView = new AircraftFareView();

                flightInfoController.initData(flightInfoView, this);

                //SearchBookingController searchBookingController = fxmlLoader.getController();
                //searchBookingController.initData(searchField.getText(), this);

                stage.show();
            }
        }
    }
}
