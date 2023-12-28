package org.but.feec.ars.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.but.feec.ars.data.SQLInjectionRepository;

public class SQLInjectionController {

    @FXML private Button dropTableButton;
    @FXML private Button createTableButton;
    @FXML private Button retrieveDataButton;
    @FXML private Button fillDataButton;

    private SQLInjectionRepository sqlInjectionRepository;

    public void initData(LogInController parentController){
        this.sqlInjectionRepository = new SQLInjectionRepository();
    }

    public void handleDropTable(ActionEvent event) {
        sqlInjectionRepository.dropTable();
    }

    public void handleCreateTable(ActionEvent event) {
        sqlInjectionRepository.createTable();
    }

    public void handleRetrieveData(ActionEvent event) {
        sqlInjectionRepository.retrieveData();
    }

    public void handleFillData(ActionEvent event) {
        sqlInjectionRepository.insertData();
    }
}
