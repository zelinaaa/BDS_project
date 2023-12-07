package org.but.feec.ars;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.but.feec.ars.config.DataSourceConfig;



import java.io.IOException;

public class App  extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        DataSourceConfig.preInit();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("App.fxml"));
        AnchorPane mainStage = null;
        try {
            mainStage = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        primaryStage.setTitle("Airline Reservation System");
        Scene scene = new Scene(mainStage);
        setUserAgentStylesheet(STYLESHEET_MODENA);
        String myStyle = getClass().getResource("css/styleLogin.css").toExternalForm();
        scene.getStylesheets().add(myStyle);

        //primaryStage.getIcons().add(new Image(App.class.getResourceAsStream("logos/mainLogo.png")));
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
