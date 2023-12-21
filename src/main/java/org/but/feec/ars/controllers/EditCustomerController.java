package org.but.feec.ars.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import org.but.feec.ars.api.CustomerCreateView;
import org.but.feec.ars.data.CustomerRepository;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;

public class EditCustomerController {

    @FXML public TextField firstNameTextField = new TextField();
    @FXML public TextField familyNameTextField;
    @FXML public TextField phoneTextField;
    @FXML public ComboBox genderComboBox;
    @FXML public ComboBox addressComboBox;
    @FXML public Button confirmButton;
    @FXML public DatePicker dateOfBirthPicker;

    private CustomerCreateView loggedUserT;
    private LoggedInController parentController;
    private CustomerRepository customerRepository;

    @FXML
    public void initData(CustomerCreateView loggedUser, LoggedInController parentController){
        this.parentController = parentController;
        this.loggedUserT = new CustomerCreateView();
        this.customerRepository = new CustomerRepository();

        CustomerCreateView customerCreateView1 = customerRepository.getCustomerInfoByEmail(loggedUser.getEmail());

        loggedUserT.setFirst_name(customerCreateView1.getFirst_name());
        loggedUserT.setFamily_name(customerCreateView1.getFamily_name());
        loggedUserT.setDate_of_birth(customerCreateView1.getDate_of_birth());
        loggedUserT.setGender(customerCreateView1.getGender());
        loggedUserT.setPhone(customerCreateView1.getPhone());
        loggedUserT.setAddress_id(customerCreateView1.getAddress_id());

        System.out.println(loggedUserT.getFirst_name() + "ahoj");


        //firstNameTextField.setText((loggedUserT.getFirst_name() == null) ? "sa" : loggedUserT.getFirst_name());
        //familyNameTextField.setText(loggedUserT.getFamily_name() != null ? loggedUserT.getFamily_name(): "");
        //dateOfBirthPicker.setValue(LocalDate.parse(loggedUserT.getDate_of_birth() != null ? loggedUserT.getDate_of_birth(): ""));
        //phoneTextField.setText(loggedUserT.getPhone() != null ? loggedUserT.getPhone(): "");
    }

    public void handleConfirm(ActionEvent event) {
        System.out.println(loggedUserT.getFirst_name()+"ahoj");
    }
}
