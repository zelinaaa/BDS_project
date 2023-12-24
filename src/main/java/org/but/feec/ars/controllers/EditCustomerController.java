package org.but.feec.ars.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import org.but.feec.ars.api.AddressDetailView;
import org.but.feec.ars.api.CustomerCreateView;
import org.but.feec.ars.data.AddressRepository;
import org.but.feec.ars.data.CustomerRepository;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EditCustomerController {

    @FXML private TextField firstNameTextField;
    @FXML private TextField familyNameTextField;
    @FXML private TextField phoneTextField;
    @FXML private ComboBox genderComboBox;
    @FXML private ComboBox addressComboBox;
    @FXML private Button confirmButton;
    @FXML private DatePicker dateOfBirthPicker;

    private CustomerCreateView loggedUser;
    private LoggedInController parentController;
    private CustomerRepository customerRepository;
    private AddressRepository addressRepository;
    private LocalDate localDate;

    ObservableList<AddressDetailView> addresses;
    ObservableList<String> genders;


    public void initData(CustomerCreateView loggedUser, LoggedInController parentController){
        this.parentController = parentController;
        this.loggedUser = loggedUser;
        this.customerRepository = new CustomerRepository();
        this.addressRepository = new AddressRepository();


        CustomerCreateView customerCreateView1 = customerRepository.getCustomerInfoByEmail(loggedUser.getEmail());


        loggedUser.setFirst_name(customerCreateView1.getFirst_name());
        loggedUser.setFamily_name(customerCreateView1.getFamily_name());
        loggedUser.setDate_of_birth(customerCreateView1.getDate_of_birth());
        loggedUser.setGender(customerCreateView1.getGender());
        loggedUser.setPhone(customerCreateView1.getPhone());
        loggedUser.setAddress_id(customerCreateView1.getAddress_id());
        loggedUser.setPerson_id(customerCreateView1.getPerson_id());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try{
            localDate = LocalDate.parse(loggedUser.getDate_of_birth(), formatter);
        } catch (Exception e){
            localDate = null;
        }

        genders = FXCollections.observableArrayList();
        addresses = addressRepository.getAllAddresses();


        genders.addAll("male", "female", "other");

        firstNameTextField.setText((loggedUser.getFirst_name() == null) ? "" : loggedUser.getFirst_name());
        familyNameTextField.setText((loggedUser.getFamily_name() == null) ? "" : loggedUser.getFamily_name());
        dateOfBirthPicker.setValue(localDate);
        phoneTextField.setText((loggedUser.getPhone() == null) ? "" : loggedUser.getPhone());
        genderComboBox.setItems(genders);
        addressComboBox.setItems(addresses);

        genderComboBox.setValue((loggedUser.getGender() == null) ? null : loggedUser.getGender());

        for (AddressDetailView addressDetailView : addresses){
            if (addressDetailView.getAddress_id() == loggedUser.getAddress_id() || addressDetailView.getAddress_id().equals(loggedUser.getAddress_id())){
                addressComboBox.setValue(addressDetailView);
                break;
            }else {
                addressComboBox.setValue(null);
            }
        }

    }

    public void handleConfirm(ActionEvent event) {
        CustomerCreateView editCustomer = new CustomerCreateView();

        String first_name = firstNameTextField.getText();
        String family_name = familyNameTextField.getText();
        String phone = phoneTextField.getText();
        String gender = String.valueOf(genderComboBox.getSelectionModel().getSelectedItem());
        AddressDetailView selectedAddress = (AddressDetailView) addressComboBox.getSelectionModel().getSelectedItem();
        Integer address_id = selectedAddress.getAddress_id();
        LocalDate localDate1 = dateOfBirthPicker.getValue();
        Integer person_id = loggedUser.getPerson_id();

        editCustomer.setPerson_id(person_id);
        editCustomer.setFirst_name(first_name);
        editCustomer.setFamily_name(family_name);
        editCustomer.setPhone(phone);
        editCustomer.setGender(gender);
        editCustomer.setAddress_id(address_id);
        editCustomer.setDate_of_birth(String.valueOf(localDate1));

        customerRepository.updateCustomer(editCustomer);

        Stage stageOld = (Stage) confirmButton.getScene().getWindow();
        stageOld.close();

    }
}
