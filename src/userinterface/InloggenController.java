package userinterface;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;
import userinterface.*;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import Utils.Database;

import javax.xml.crypto.Data;

import static javafx.application.Application.launch;

public class InloggenController {
    @FXML
    private Label HeadLabel;
    @FXML
    private TextField wachtwoord;
    @FXML
    private TextField gebruikersnaam;

    public InloggenController() {
    }

    private void initialize() {
    }

    public void CheckLogIn(ActionEvent actionEvent) {
        String gebrnm = String.valueOf(gebruikersnaam);
        String wachtw = String.valueOf(wachtwoord);

        ArrayList<Map<String, Object>> gebrD = Database.executeStatement("SELECT g.gebruikersnaam FROM gebruiker g WHERE g.gebruikersnaam = " + gebrnm);
        ArrayList<Map<String, Object>> wwD = Database.executeStatement("SELECT g.wachtwoord FROM gebruiker g WHERE g.wachtwoord = " + wachtw + " AND g.gebruikersnaam = " + gebrnm);
        if (gebrD.size() == 1){
            if (wwD.size() == 1) {
                mainmenuController.setUsernaam(gebrnm);
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Mainmenu.fxml"));
                    Parent root = loader.load();
                    mainmenuController controller = loader.getController();

                    Stage newStage = new Stage();
                    newStage.setScene(new Scene(root));
                    newStage.setTitle("Main menu");
                    newStage.initModality(Modality.APPLICATION_MODAL);
                    newStage.showAndWait();
                    initialize();

                } catch (IOException e) {
                    String message = e.getMessage();
                    HeadLabel.setText(message);
                }
            } else {
                HeadLabel.setText("Dit wachtwoord bestaat niet.");
            }
        } else {
            HeadLabel.setText("Deze gebruiker bestaat niet.");
        }
    }


//    public static void main(String[] args) {
//        launch(args);
//    }


}
