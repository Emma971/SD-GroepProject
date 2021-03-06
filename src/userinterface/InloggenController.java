package userinterface;

import Utils.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class InloggenController {

    public Button loginButton;
    @FXML private Label HeadLabel;
    @FXML private TextField wachtwoord;
    @FXML private TextField gebruikersnaam;

    public void logIn() throws IOException {
        String gebrnm = gebruikersnaam.getText();
        String wachtw = wachtwoord.getText();

        String query = "SELECT COUNT(*) " +
                "FROM gebruiker g " +
                "WHERE g.gebruikersnaam = '" + gebrnm + "'";
        try {
            ArrayList<Map<String, Object>> gebrD = new ArrayList<>(Database.executeStatement(query));
            if ((int) gebrD.get(0).get("COUNT(*)") == 1) {
                query = "SELECT g.gebruikerType, k.klasNaam, g.gebruikerID, g.naam, m.medewerkerType, l.leerlingID, m.medewerkerID " +
                        "FROM gebruiker g " +
                        "LEFT OUTER JOIN medewerker m ON g.gebruikerID = m.gebruikerID " +
                        "LEFT OUTER JOIN leerling l ON g.gebruikerID = l.gebruikerID " +
                        "LEFT OUTER JOIN klas k ON l.klasID = k.klasID " +
                        "WHERE g.wachtwoord = '" + wachtw + "' " +
                        "AND g.gebruikersnaam = '" + gebrnm + "'";
                ArrayList<Map<String, Object>> wwD = Database.executeStatement(query);
                System.out.println(wwD);
                if (wwD.size() == 1) {
                    int gebruikerID = (int) wwD.get(0).get("gebruikerID");
                    String naam = (String) wwD.get(0).get("naam");
                    String gebruikerType = (String) wwD.get(0).get("gebruikerType");
                    int typeID;
                    if (gebruikerType.equals("medewerker")) {
                        gebruikerType = (String) wwD.get(0).get("medewerkerType");
                        typeID = (int) wwD.get(0).get("medewerkerID");
                    } else {
                        typeID = (int) wwD.get(0).get("leerlingID");
                    }
                    String klasNaam = (String) wwD.get(0).get("klasNaam");
                    Gebruiker gebruiker = new Gebruiker(gebruikerID, naam, gebruikerType, typeID, klasNaam);
                    //                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
                    Parent root = loader.load();
                    MainMenuController controller = loader.getController();
                    controller.setGebruiker(gebruiker);
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Main menu");
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.show();
                    root.requestFocus();

                    //                } catch (IOException e) {
                    //                    String message = e.getMessage();
                    //                    HeadLabel.setText(message);
                    //                }
                } else {
                    HeadLabel.setText("Onjuist wachtwoord");
                }
            } else {
                HeadLabel.setText("Deze gebruiker bestaat niet");
            }
        } catch (UnsupportedOperationException e) {
            Popup.alert(e.getLocalizedMessage());
        }
    }
}
