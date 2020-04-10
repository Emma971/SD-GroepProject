package userinterface;

import Utils.Database;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
                query = "SELECT g.gebruikerType, k.klasNaam, g.gebruikerID, g.naam, m.medewerkerType " +
                        "FROM gebruiker g " +
                        "LEFT OUTER JOIN medewerker m ON g.gebruikerID = m.gebruikerID " +
                        "LEFT OUTER JOIN leerling l ON g.gebruikerID = l.gebruikerID " +
                        "LEFT OUTER JOIN klas k ON l.klasID = k.klasID " +
                        "WHERE g.wachtwoord = '" + wachtw + "' " +
                        "AND g.gebruikersnaam = '" + gebrnm + "'";
                ArrayList<Map<String, Object>> wwD = Database.executeStatement(query);
                System.out.println(wwD);
                if (wwD.size() == 1) {
                    //                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Mainmenu.fxml"));
                    Parent root = loader.load();
                    mainmenuController controller = loader.getController();
                    String gebruikertype = (String) wwD.get(0).get("gebruikerType");
                    if (gebruikertype.equals("medewerker")) {
                        gebruikertype = (String) wwD.get(0).get("medewerkerType");
                    }
                    controller.setLoginDetails((int) wwD.get(0).get("gebruikerID"), (String) wwD.get(0).get("naam"), gebruikertype, (String) wwD.get(0).get("klasNaam"));
                    Stage newStage = new Stage();
                    newStage.setScene(new Scene(root));
                    newStage.setTitle("Main menu");
                    newStage.initModality(Modality.APPLICATION_MODAL);
                    newStage.showAndWait();

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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Er is een fout opgetreden");
            alert.setHeaderText(null);
            alert.setContentText(e.getLocalizedMessage());
            alert.showAndWait();
        }
    }
}
