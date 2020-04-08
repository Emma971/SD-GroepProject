package userinterface;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class mainmenuController {

    public Label loginLabel;
    public Button closebutton;
    @FXML private Label loggedLabel;
    @FXML private Label errormenulabel;

    @FXML private Button studentrooster;
    @FXML private Button studentabsent;
    @FXML private Button studentaanwezigheid;

    @FXML private Button docentrooster;
    @FXML private Button docentpresent;
    @FXML private Button SBtoevoegen;

    private static String username;
    private static String usertype;
    private static String klasnaam;
    private static int userID;

    public void setLoginDetails(String username, String usertype, String klasnaam) {
        mainmenuController.username = username;
        mainmenuController.usertype = usertype;
        mainmenuController.klasnaam = klasnaam;
    }

    public void initialize() {

        String userlabel = username + ", " + usertype;

        studentrooster.setVisible      (                usertype.equals("leerling") ||
                        usertype.equals("slb"));
        studentabsent.setVisible       (usertype.equals("leerling"));
        studentaanwezigheid.setVisible (usertype.equals("leerling") || usertype.equals("decaan") || usertype.equals("slb"));

        docentrooster.setVisible       (usertype.equals(  "docent"));
        docentpresent.setVisible       (usertype.equals(  "docent"));

        SBtoevoegen.setVisible        (usertype.equals("beheerder"));


        if (usertype.equals("leerling")){
            userlabel = userlabel + " van klas: " + klasnaam;
        }
        if (usertype.equals("SLB")){
            userlabel = userlabel + " van klas: " + klasnaam;
        }
        loggedLabel.setText(userlabel);
    }

    public void roosterscherm(ActionEvent actionEvent) {
        if(usertype.equals("leerling")) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("SchoolOverzicht.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setTitle("Rooster");
                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
            } catch (IOException e) {
                errormenulabel.setText("ERROR! Couldn't load new window.");
            }
        }
        else if(usertype.equals("docent")){
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("DocentRooster.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setTitle("Rooster");
                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
            } catch (IOException e) {
                errormenulabel.setText("ERROR! Couldn't load new window.");
            }
        }
    }

    public void aanwezig(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("aanwezigheid.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Aanwezigheid");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            errormenulabel.setText("ERROR! Couldn't load new window.");
        }
    }

    public void sbUserToevoegen(ActionEvent actionEvent) {
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

    public void absentmelden(ActionEvent actionEvent) {
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

    public void presentieLijst(ActionEvent actionEvent) {
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

    public void close(ActionEvent actionEvent) {
        Platform.exit();
    }

    public static void setUsername(String naam){
        username = naam;
    }

    public static void setUsertype(String type){
        usertype = type;
    }

    public static void setUserklasnaam(String naam){
        klasnaam = naam;
    }

    public static void setUserID(int ID){
        userID = ID;
    }


    public static String getUsername(){
        return username;
    }

    public static String getUsertype(){
        return usertype;
    }

    public static  String getUserklasnaam(){
        return klasnaam;
    }

    public static  int getUserID(){
        return userID;
    }
}