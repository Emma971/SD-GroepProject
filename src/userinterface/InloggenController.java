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

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class InloggenController {

    public Button loginButton;
    @FXML private Label HeadLabel;
    @FXML private TextField wachtwoord;
    @FXML private TextField gebruikersnaam;

    public InloggenController() {
    }

    private void initialize() {
    }

    public void logIn(ActionEvent actionEvent) throws IOException {
        String gebrnm = gebruikersnaam.getText();
        String wachtw = wachtwoord.getText();

        ArrayList<Map<String, Object>> gebrD = new ArrayList<>(Database.executeStatement("SELECT COUNT(*) FROM gebruiker g WHERE g.gebruikersnaam = '" + gebrnm+"';"));

        System.out.println(gebrD);
        if ((int) gebrD.get(0).get("COUNT(*)") == 1){
            ArrayList<Map<String, Object>> wwD = Database.executeStatement("SELECT g.type, k.klasNaam, g.gebruikerID, g.naam FROM gebruiker g LEFT OUTER JOIN leerling l ON g.gebruikerID = l.gebruikerID LEFT OUTER JOIN klas k ON l.klasID = k.klasID WHERE g.wachtwoord = '" + wachtw + "' AND g.gebruikersnaam = '" + gebrnm+"';");
            System.out.println(wwD);
            if (wwD.size() == 1) {
//                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Mainmenu.fxml"));
                    Parent root = loader.load();
                    mainmenuController controller = loader.getController();
                    controller.setLoginDetails((int) wwD.get(0).get("gebruikerID"), (String) wwD.get(0).get("naam"), (String) wwD.get(0).get("type"), (String) wwD.get(0).get("klasNaam"));
                    Stage newStage = new Stage();
                    newStage.setScene(new Scene(root));
                    newStage.setTitle("Main menu");
                    newStage.initModality(Modality.APPLICATION_MODAL);
                    newStage.showAndWait();
                    initialize();

//                } catch (IOException e) {
//                    String message = e.getMessage();
//                    HeadLabel.setText(message);
//                }
            } else {
                HeadLabel.setText("Dit wachtwoord bestaat niet.");
            }
        } else {
            HeadLabel.setText("Deze gebruiker bestaat niet.");
        }
    }
}
