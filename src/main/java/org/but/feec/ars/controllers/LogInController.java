package org.but.feec.ars.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.but.feec.ars.App;
import javafx.fxml.FXML;
import org.but.feec.ars.api.CustomerCreateView;
import org.but.feec.ars.data.CustomerRepository;
import org.but.feec.ars.services.AuthService;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Console;
import java.io.IOException;

public class LogInController {

    private static final Logger logger = LoggerFactory.getLogger(LogInController.class);

    @FXML
    public TextField emailTextField;
    @FXML
    public PasswordField passwordTextField;
    @FXML
    private Button logInButton;
    @FXML
    private Hyperlink signInHyperlink;
    @FXML
    private Button sqlInjectionButton;


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

        ValidationSupport validation = new ValidationSupport();
        validation.registerValidator(emailTextField, Validator.createEmptyValidator("Enter email address!"));
        validation.registerValidator(passwordTextField, Validator.createEmptyValidator("Enter password!"));
        logInButton.disableProperty().bind(validation.invalidProperty());

        boolean authentication = authService.authenticate(email, password);

        Integer role = customerRepository.getRoleID(email);


        if (authentication && role == 1) {
            CustomerCreateView userLogged = new CustomerCreateView();
            userLogged.setEmail(email);
            userLogged.setPassword(password.toCharArray());

            CustomerCreateView loggedUserWithInfo = customerRepository.getCustomerInfoByEmail(userLogged.getEmail());
            logger.info(String.format("User %d logged in as customer.", loggedUserWithInfo.getPerson_id()));

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(App.class.getResource("fxml/Main.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                logger.error("Error in loading new scene: " + e.getMessage());
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

        } else if (authentication && role == 2) {
            CustomerCreateView userLogged = new CustomerCreateView();
            userLogged.setEmail(email);
            userLogged.setPassword(password.toCharArray());

            CustomerCreateView loggedUserWithInfo = customerRepository.getCustomerInfoByEmail(userLogged.getEmail());
            logger.info(String.format("User %d logged in as administrator.", loggedUserWithInfo.getPerson_id()));

            System.out.println("administrator");
        } else {
            logger.error(String.format("Entered wrong credentials with email %s", email));
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login failed!");
            alert.setHeaderText("Username or password is not valid");
            alert.showAndWait();
        }
    }

    private void showSignIn(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(App.class.getResource("fxml/SignIn.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            logger.error("Error in loading new scene: " + e.getMessage());
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
    }

    public void signInActionHandler() {
        showSignIn();
    }

    public void handleSQLInjection(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(App.class.getResource("fxml/SQLInjection.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            logger.error("Error in loading new scene: " + e.getMessage());
            throw new RuntimeException(e);
        }
        Stage stage = new Stage();
        stage.setTitle("SQL Injection Simulation");
        stage.setScene(scene);

        SQLInjectionController sqlInjectionController = fxmlLoader.getController();
        sqlInjectionController.initData(this);

        stage.show();
    }
}
