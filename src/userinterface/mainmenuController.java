package userinterface;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class mainmenuController {

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

    public void setLoginDetails(int gebruikerID, String username, String usertype, String klasnaam) {
        this.gebruikerID = gebruikerID;
        this.naamGebruiker = username;
        this.usertype = usertype;
        this.klasnaam = klasnaam;



        String userlabel = username + ", " + usertype;

        studentrooster.setVisible      (usertype.equals("leerling") || usertype.equals("slb"));
        studentabsent.setVisible       (usertype.equals("leerling"));
        studentaanwezigheid.setVisible (usertype.equals("leerling") || usertype.equals("decaan"));

        docentrooster.setVisible       (usertype.equals(  "docent"));
        docentpresent.setVisible       (false);  // (usertype.equals(  "docent"));

        SBtoevoegen.setVisible         (usertype.equals("beheerder"));


        if (usertype.equals("leerling")){
            userlabel = userlabel + " van klas: " + klasnaam;
        }
        if (usertype.equals("SLB")){
            userlabel = userlabel + " van klas: " + klasnaam;
        }
        loggedLabel.setText(userlabel);
    }

    public void roosterscherm() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            Scene scene = null;
            if(usertype.equals("leerling")) {
                fxmlLoader.setLocation(getClass().getResource("SchoolOverzicht.fxml"));
                scene = new Scene(fxmlLoader.load());
                SchoolOverzichtController controller = fxmlLoader.getController();
                controller.setParentController(this);
            } else if (usertype.equals("docent")|| usertype.equals("slb")) {
                fxmlLoader.setLocation(getClass().getResource("DocentRooster.fxml"));
                scene = new Scene(fxmlLoader.load());
                DocentRoosterController controller = fxmlLoader.getController();
                controller.setParentController(this);
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
            controller.setParentController(this);
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


    public String getNaamGebruiker(){
        return naamGebruiker;
    }

    public String getUsertype(){
        return usertype;
    }

    public String getUserklasnaam(){
        return klasnaam;
    }

    public int getGebruikerID(){
        return gebruikerID;
    }
}