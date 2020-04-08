package userinterface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

import static Utils.Database.executeStatement;

public class UserToevoegenController {

    @FXML private Label sbLeerlingGIDLabel;
    @FXML private Label sbMedewerkerGIDLabel;
    @FXML private Label sbSLBMEDIDLabel;
    @FXML private Label sbDocentMEDIDLabel;
    @FXML private Label errorLabel;

    @FXML private TextField sbNaamtext;
    @FXML private TextField sbWachtword;
    @FXML private TextField sbLeerlingGID;
    @FXML private TextField sbMedewerkerGID;
    @FXML private TextField sbSLBMedID;
    @FXML private TextField sbDocentMedID;

    @FXML private ComboBox sbUsertype;
    @FXML private ComboBox sbLeerlingKlas;
    @FXML private ComboBox sbMedewerkerType;
    @FXML private ComboBox sbSLBKlas;
    @FXML private ComboBox sbDocentCursus;

    public void initialize() {
           try {
               ObservableList<String> usertype = FXCollections.observableArrayList("leerling","medewerker");
               sbUsertype.setItems(usertype);

               ObservableList<String> medewerkertype = FXCollections.observableArrayList("docent", "slb","decaan","beheerder");
               sbMedewerkerType.setItems(medewerkertype);

               ObservableList<String> klassen = FXCollections.observableArrayList();
               ObservableList<String> cursus = FXCollections.observableArrayList();

               for (Map<String, Object> data : executeStatement("SELECT klas.klasNaam, cursus.cursusNaam FROM klas LEFT OUTER JOIN cursus ON klas.klasID = cursus.klasID UNION SELECT klas.klasNaam, cursus.cursusNaam FROM klas RIGHT OUTER JOIN cursus ON klas.klasID = cursus.klasID;")){
                  String klasNaam   = (String) data.get("klasNaam");
                  String cursusnaam = (String) data.get("cursusNaam");
                  if(klasNaam!=null){
                  klassen.add(klasNaam);
                  }
                  if(cursusnaam!=null){
                  cursus.add(cursusnaam);}
               }
               sbLeerlingKlas.setItems(klassen);
               sbSLBKlas.setItems(klassen);
               sbDocentCursus.setItems(cursus);
           }catch (NullPointerException e){
               e.printStackTrace();
           }
    }

    public void action(ActionEvent actionEvent) {
        String userComboValue = (String) sbUsertype.getValue();
                        sbLeerlingGIDLabel.setVisible(userComboValue.equals("leerling"));
                        sbLeerlingGID.setVisible(userComboValue.equals("leerling"));
                        sbLeerlingKlas.setVisible(userComboValue.equals("leerling"));

                        sbMedewerkerGIDLabel.setVisible(userComboValue.equals("medewerker"));
                        sbMedewerkerGID.setVisible(userComboValue.equals("medewerker"));
                        sbMedewerkerType.setVisible(userComboValue.equals("medewerker"));
        if (userComboValue==null){
            sbLeerlingGIDLabel.setVisible(userComboValue.equals("\"LOL\""));
            sbLeerlingGID.setVisible(userComboValue.equals("\"LOL\""));
            sbLeerlingKlas.setVisible(userComboValue.equals("\"LOL\""));

            sbMedewerkerGIDLabel.setVisible(userComboValue.equals("\"LOL\""));
            sbMedewerkerGID.setVisible(userComboValue.equals("\"LOL\""));
            sbMedewerkerType.setVisible(userComboValue.equals("\"LOL\""));
        }
    }
    public void action2(ActionEvent actionEvent){
        String medwerkerComboValue = (String) sbMedewerkerType.getValue();
               sbSLBMEDIDLabel.setVisible(medwerkerComboValue.equals("slb"));
               sbSLBMedID.setVisible(medwerkerComboValue.equals("slb"));
               sbSLBKlas.setVisible(medwerkerComboValue.equals("slb"));

               sbDocentMEDIDLabel.setVisible(medwerkerComboValue.equals("docent"));
               sbDocentMedID.setVisible(medwerkerComboValue.equals("docent"));
               sbDocentCursus.setVisible(medwerkerComboValue.equals("docent"));
               if (medwerkerComboValue==null){
                   sbSLBMEDIDLabel.setVisible(medwerkerComboValue.equals("LOL"));
                   sbSLBMedID.setVisible(medwerkerComboValue.equals("LOL"));
                   sbSLBKlas.setVisible(medwerkerComboValue.equals("LOL"));

                   sbDocentMEDIDLabel.setVisible(medwerkerComboValue.equals("LOL"));
                   sbDocentMedID.setVisible(medwerkerComboValue.equals("LOL"));
                   sbDocentCursus.setVisible(medwerkerComboValue.equals("LOL"));
               }
    }
    public void opslaan(ActionEvent actionEvent){

    }
    
    public void reset(ActionEvent actionEvent){
        sbDocentCursus.setValue("");
        sbSLBKlas.setValue("");
        sbMedewerkerType.setValue("");
        sbLeerlingKlas.setValue("");
        sbUsertype.setValue("");
        sbMedewerkerGID.setText(null);
        sbLeerlingGID.setText(null);
        sbSLBMedID.setText(null);
        sbDocentMedID.setText(null);
        sbNaamtext.setText(null);
        sbWachtword.setText(null);
    }
}

