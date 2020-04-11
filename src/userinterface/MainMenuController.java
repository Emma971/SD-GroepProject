package userinterface;

import Utils.Gebruiker;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

    private String naamGebruiker;
    private String usertype;
    private String klasnaam;
    private int gebruikerID;
    private Gebruiker gebruiker;

    public void setLoginDetails(int gebruikerID, String username, String usertype, String klasnaam) {
        this.gebruikerID = gebruikerID;
        this.naamGebruiker = username;
        this.usertype = usertype;
        this.klasnaam = klasnaam;
    }

    public void roosterscherm() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            Scene scene = null;
            if(usertype.equals("leerling")) {
                fxmlLoader.setLocation(getClass().getResource("SchoolOverzicht.fxml"));
                scene = new Scene(fxmlLoader.load());
                SchoolOverzichtController controller = fxmlLoader.getController();
                controller.setGebruiker(gebruiker);
//                controller.setParentController(this);
            } else if (usertype.equals("docent")|| usertype.equals("slb")) {
                fxmlLoader.setLocation(getClass().getResource("DocentRooster.fxml"));
                scene = new Scene(fxmlLoader.load());
                DocentRoosterController controller = fxmlLoader.getController();
                controller.setGebruiker(gebruiker);
//                controller.setParentController(this);
                controller.setUserType(usertype);
            }
            Stage stage = new Stage();
            stage.setTitle("Rooster");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            errormenulabel.setText("ERROR! Couldn't load new window.");
        }
    }

    public void aanwezig() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("aanwezigheid.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            aanwezigheidController controller = fxmlLoader.getController();
            controller.setGebruiker(gebruiker);
//            controller.setParentController(this);
            Stage stage = new Stage();
            stage.setTitle("Aanwezigheid");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            errormenulabel.setText("ERROR! Couldn't load new window.");
        }
    }

    public void sbUserToevoegen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("UserToevoegen.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Aanwezigheid");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            errormenulabel.setText("ERROR! Couldn't load new window.");
            e.printStackTrace();
        }
    }

    public void absentmelden() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("AfmeldenLes.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            aanwezigheidController controller = fxmlLoader.getController();
            controller.setGebruiker(gebruiker);
//            controller.setParentController(this);
            Stage stage = new Stage();
            stage.setTitle("Absent Melden");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            errormenulabel.setText("ERROR! Couldn't load new window.");
        }
    }

    public void presentieLijst() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("AfwezigeStudentenShow.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            aanwezigheidController controller = fxmlLoader.getController();
            controller.setGebruiker(gebruiker);
//            controller.setParentController(this);
            Stage stage = new Stage();
            stage.setTitle("presentieLijst");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            errormenulabel.setText("ERROR! Couldn't load new window.");
        }
    }

    public void close() {
        Platform.exit();
    }

    public void setGebruiker(Gebruiker gebruiker) {
        this.gebruiker = gebruiker;
        String gebruikerType = gebruiker.getType();
        String userlabel = gebruiker.getNaam() + ", " + gebruiker.getType();

        studentrooster.setVisible      (gebruikerType.equals("leerling") || gebruikerType.equals("slb"));
        studentabsent.setVisible       (gebruikerType.equals("leerling"));
        studentaanwezigheid.setVisible (gebruikerType.equals("leerling") || gebruikerType.equals("decaan") || gebruikerType.equals("slb") );

        docentrooster.setVisible       (gebruikerType.equals(  "docent"));
        docentpresent.setVisible       (false);  // (usertype.equals(  "docent"));

        SBtoevoegen.setVisible         (gebruikerType.equals("beheerder"));


        if (gebruikerType.equals("leerling")){
            userlabel = userlabel + " van klas: " + gebruiker.getKlasNaam();
        }
        loggedLabel.setText(userlabel);
    }
}