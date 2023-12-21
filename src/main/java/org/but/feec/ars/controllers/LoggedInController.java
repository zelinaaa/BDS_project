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

import java.io.IOException;

public class LoggedInController {
    private CustomerCreateView loggedUser;
    private LogInController parentController;
    
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
        //flightid.setText(customerCreateView.getEmail());
    }

    public void handleAbout(ActionEvent event) {
        
    }

    public void handleContacs(ActionEvent event) {
    }

    public void handleLogOut(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(App.class.getResource("App.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
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
            throw new RuntimeException(e);
        }
        Stage stage = new Stage();
        stage.setTitle("Account Settings");
        stage.setScene(scene);
        EditCustomerController editCustomerController = new EditCustomerController();
        editCustomerController.initData(loggedUser, this);
        stage.show();
    }

    public void handleMyBooking(ActionEvent event) {
    }

    public void handleSearch(ActionEvent event) {
    }
}
