package org.but.feec.ars.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.but.feec.ars.App;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import org.but.feec.ars.api.CustomerCreateView;
import org.but.feec.ars.data.CustomerRepository;
import org.but.feec.ars.services.AuthService;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import java.io.Console;
import java.io.IOException;

public class LogInController {

    @FXML
    public TextField emailTextField;
    @FXML
    public TextField passwordTextField;
    @FXML
    private Button logInButton;
    @FXML
    private Hyperlink signInHyperlink;


    private AuthService authService;
    private CustomerRepository customerRepository;
    private CustomerCreateView customerCreateView;


    public LogInController(){}

    @FXML
    private void initialize(){
        initializeServices();
    }
    private void handleLogIn(){
        String email = emailTextField.getText();
        String password = passwordTextField.getText();

        boolean authentication = authService.authenticate(email, password);
        if (authentication) {
            System.out.println("uspech");

            CustomerCreateView userLogged = new CustomerCreateView();
            userLogged.setEmail(email);
            userLogged.setPassword(password.toCharArray());

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(App.class.getResource("fxml/Main.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Stage stage = new Stage();

            stage.setTitle("You are logged in!");
            stage.setScene(scene);

            Stage stageOld = (Stage) logInButton.getScene().getWindow();

            LoggedInController loggedInController = fxmlLoader.getController();
            loggedInController.initdata(userLogged, this);

            stageOld.close();
            stage.show();

        }else {
            System.out.println("not uspech");
        }
    }

    private void showSignIn(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(App.class.getResource("fxml/SignIn.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = new Stage();
        stage.setTitle("Sign In window");
        stage.setScene(scene);

        stage.show();
    }

    private void initializeServices() {
        customerRepository = new CustomerRepository();
        authService = new AuthService(customerRepository);
    }

    public void logInActionHandler(){
        handleLogIn();
        //System.out.println("pressed");
    }

    public void signInActionHandler() {
        showSignIn();
    }
}
