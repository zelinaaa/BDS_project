package org.but.feec.ars.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.but.feec.ars.api.CustomerCreateView;
import org.but.feec.ars.data.CustomerRepository;
import org.but.feec.ars.services.CustomerService;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import java.util.Optional;
import javafx.util.Duration;

import java.awt.*;

public class SignInController {
    @FXML
    private TextField email_signin;
    @FXML
    private PasswordField password_signin;
    @FXML
    private Button button;

    private CustomerRepository customerRepository;
    private CustomerService customerService;
    private ValidationSupport validation;

    @FXML
    public void initialize(){
        customerRepository = new CustomerRepository();
        customerService = new CustomerService(customerRepository);

        validation = new ValidationSupport();
        validation.registerValidator(email_signin, Validator.createEmptyValidator("The email must not be empty."));
        validation.registerValidator(password_signin, Validator.createEmptyValidator("The password must not be empty."));

    }

    @FXML
    public void handleSignIn(ActionEvent event) {
        String email = email_signin.getText();
        String password = password_signin.getText();

        CustomerCreateView customerCreateView = new CustomerCreateView();
        customerCreateView.setEmail(email);
        customerCreateView.setPassword(password.toCharArray());

        customerService.createCustomer(customerCreateView);

        handleGoodSignIn();
    }

    private void handleGoodSignIn(){
        Stage stageOld = (Stage) button.getScene().getWindow();
        stageOld.close();

    }
}
