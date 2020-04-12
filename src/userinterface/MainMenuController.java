package userinterface;

import Utils.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {

    @FXML private Label loggedLabel;
    @FXML private Label errormenulabel;

    @FXML private Button studentrooster;
    @FXML private Button studentabsent;
    @FXML private Button studentaanwezigheid;

    @FXML private Button docentrooster;
    @FXML private Button docentpresent;
    @FXML private Button SBtoevoegen;
    private Gebruiker gebruiker;

    public void setGebruiker(Gebruiker gebruiker) {
        this.gebruiker = gebruiker;
        String gebruikerType = gebruiker.getType();
        String userlabel = gebruiker.getNaam() + ", " + gebruiker.getType();

        studentrooster.setVisible      (gebruikerType.equals("leerling") || gebruikerType.equals("slb"));
        studentabsent.setVisible       (false);  //(gebruikerType.equals("leerling"));
        studentaanwezigheid.setVisible (gebruikerType.equals("leerling") || gebruikerType.equals("decaan") || gebruikerType.equals("slb") );

        docentrooster.setVisible       (gebruikerType.equals(  "docent"));
        docentpresent.setVisible       (false);  // (gebruiker.getType().equals(  "docent"));

        SBtoevoegen.setVisible         (gebruikerType.equals("beheerder"));


        if (gebruikerType.equals("leerling")){
            userlabel = userlabel + " van klas: " + gebruiker.getKlasNaam();
        }
        loggedLabel.setText(userlabel);
    }

    public void roosterscherm() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root;
            Scene scene = null;
            if(gebruiker.getType().equals("leerling")) {
                fxmlLoader.setLocation(getClass().getResource("SchoolOverzicht.fxml"));
                root = fxmlLoader.load();
                scene = new Scene(root);
                SchoolOverzichtController controller = fxmlLoader.getController();
                controller.setGebruiker(gebruiker);
//                controller.setParentController(this);
            } else if (gebruiker.getType().equals("docent")|| gebruiker.getType().equals("slb")) {
                fxmlLoader.setLocation(getClass().getResource("DocentRooster.fxml"));
                root = fxmlLoader.load();
                scene = new Scene(root);
                DocentRoosterController controller = fxmlLoader.getController();
                controller.setGebruiker(gebruiker);
//                controller.setParentController(this);
//                controller.setUserType(gebruiker.getType());
            } else {
                root = fxmlLoader.load();  // Unreachable, but circumvents compile error
            }
            Stage stage = new Stage();
            stage.setTitle("Rooster");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
            root.requestFocus();

        } catch (IOException e) {
            errormenulabel.setText("ERROR! Couldn't load new window.");
        }
    }

    public void aanwezig() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("aanwezigheid.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            aanwezigheidController controller = fxmlLoader.getController();
            controller.setGebruiker(gebruiker);
            Stage stage = new Stage();
            stage.setTitle("Aanwezigheid");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
            root.requestFocus();
        } catch (IOException e) {
            Popup.alert("ERROR! Couldn't load new window.");
        }
    }

    public void sbUserToevoegen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("UserToevoegen.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Aanwezigheid");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
            root.requestFocus();
        } catch (IOException e) {
            errormenulabel.setText("ERROR! Couldn't load new window.");
            e.printStackTrace();
        }
    }

    public void absentmelden() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("AfmeldenLes.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            aanwezigheidController controller = fxmlLoader.getController();
            controller.setGebruiker(gebruiker);
//            controller.setParentController(this);
            Stage stage = new Stage();
            stage.setTitle("Absent Melden");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
            root.requestFocus();
        } catch (IOException e) {
            errormenulabel.setText("ERROR! Couldn't load new window.");
        }
    }

    public void presentieLijst() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("AfwezigeStudentenShow.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            aanwezigheidController controller = fxmlLoader.getController();
            controller.setGebruiker(gebruiker);
//            controller.setParentController(this);
            Stage stage = new Stage();
            stage.setTitle("presentieLijst");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
            root.requestFocus();
        } catch (IOException e) {
            errormenulabel.setText("ERROR! Couldn't load new window.");
        }
    }

//    public void close() {
//        Platform.exit();
//    }
}