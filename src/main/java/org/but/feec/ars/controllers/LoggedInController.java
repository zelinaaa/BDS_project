package org.but.feec.ars.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.but.feec.ars.App;
import org.but.feec.ars.api.CustomerCreateView;
import org.but.feec.ars.data.BookingRepository;
import org.but.feec.ars.data.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class LoggedInController {

    private static final Logger logger = LoggerFactory.getLogger(LoggedInController.class);
    private CustomerCreateView loggedUser;
    private LogInController parentController;

    private CustomerCreateView loggedUserWithInfo;
    private CustomerRepository customerRepository;
    
    @FXML private Button aboutButton;
    @FXML private Button contactsButton;
    @FXML private Button logoutButton;
    @FXML private Button helpButton;
    @FXML private Button accountSettingsButton;
    @FXML private Button myBookingButton;
    @FXML private Button searchButton;
    @FXML private TextField searchField;


    public void initdata(CustomerCreateView loggedUser, LogInController parentController){
        this.parentController = parentController;
        this.loggedUser = loggedUser;
        this.customerRepository = new CustomerRepository();
        this.loggedUserWithInfo = customerRepository.getCustomerInfoByEmail(loggedUser.getEmail());
    }

    public void handleAbout(ActionEvent event) {
    }

    public void handleContacs(ActionEvent event) {
    }

    public void handleLogOut(ActionEvent event) {
        logger.info(String.format("User %d logged out.", loggedUserWithInfo.getPerson_id()));
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(App.class.getResource("App.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            logger.error("Error in loading new scene: " + e.getMessage());
            throw new RuntimeException(e);
        }
        Stage stage = new Stage();
        stage.setTitle("Airline Reservation System");
        stage.setScene(scene);
        Stage stageOld = (Stage) logoutButton.getScene().getWindow();
        stageOld.close();
        stage.show();
    }

    public void handleHelp(ActionEvent event) {
    }

    public void handleAccountSettings(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(App.class.getResource("fxml/AccountSettings.fxml"));
        Scene scene = null;
        try{
            scene = new Scene(fxmlLoader.load());
        }catch (IOException e){
            logger.error("Error in loading new scene: " + e.getMessage());
            throw new RuntimeException(e);
        }
        Stage stage = new Stage();
        stage.setTitle("Account Settings");
        stage.setScene(scene);

        EditCustomerController editCustomerController = fxmlLoader.getController();
        editCustomerController.initData(loggedUser, this);

        stage.show();
    }

    public void handleMyBooking(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(App.class.getResource("fxml/MyBookings.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            logger.error("Error in loading new scene: " + e.getMessage());
            throw new RuntimeException(e);
        }
        Stage stage = new Stage();
        stage.setTitle("My bookings");
        stage.setScene(scene);

        MyBookingsController myBookingsController = fxmlLoader.getController();
        myBookingsController.initData(loggedUserWithInfo, this);

        stage.show();
    }

    public void handleSearch(ActionEvent event) {
        logger.info(String.format("User %d search for a flight.", loggedUserWithInfo.getPerson_id()));
        showSearch(event, searchField.getText());
    }

    public void handleSearchAll(ActionEvent event) {
        logger.info(String.format("User %d search for all flights", loggedUserWithInfo.getPerson_id()));
        showSearch(event, "");
    }

    private void showSearch(ActionEvent event, String searched){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(App.class.getResource("fxml/SearchBooking.fxml"));
        Scene scene = null;
        try{
            scene = new Scene(fxmlLoader.load());
        }catch (IOException e){
            logger.error("Error in loading new scene: " + e.getMessage());
            throw new RuntimeException(e);
        }
        Stage stage = new Stage();
        stage.setTitle("Search Bookings");
        stage.setScene(scene);

        SearchBookingController searchBookingController = fxmlLoader.getController();
        searchBookingController.initData(loggedUserWithInfo, searched, this);

        stage.show();
    }
}
