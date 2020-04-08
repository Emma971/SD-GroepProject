package userinterface;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;

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
    //private DatabaseTest data;
    @FXML
    private TextField wachtwoord;
    @FXML
    private TextField gebruikersnaam;

    public InloggenController() {
    }

    public void CheckLogIn(ActionEvent actionEvent) {
        String gebr;
        String ww;
        for (gebr = String.valueOf(Database.executeStatement("SELECT g.gebruikersnaam FROM gebruiker g"))){
            if (this.gebruikersnaam.equals(gebr)){
                for (ww = String.valueOf(Database.executeStatement("SELECT g.wachtwoord FROM gebruiker g"))) {
                    if (this.wachtwoord.equals(ww)) {
//                        try {
//                            FXMLLoader loader = new FXMLLoader(getClass().getResource(""));
//                            Parent root = loader.load();
//                             controller = loader.getController();
//
//                            Stage newStage = new Stage();
//                            newStage.setScene(new Scene(root));
//                            newStage.setTitle("Nieuwe boeking");
//                            newStage.initModality(Modality.APPLICATION_MODAL);
//                            newStage.showAndWait();
//                            initialize();
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                    }
                }
            }
        }
//        if (this.wachtwoord.equals(data.getWachtwoord()) && this.gebruikersnaam.equals(data.getGebruikersnaam())) {
//           // ga na scherm 2
//           // dit is niets, ik moest wat invullen
//           String s = "";
//       } else {
//            // show error scherm
//            String t = "";
//        }
    }

    private void initialize() {
    }

    public static void main(String[] args) {
        launch(args);
        Database.executeStatement("SELECT g.gebruikersnaam FROM gebruiker g");
    }


}
