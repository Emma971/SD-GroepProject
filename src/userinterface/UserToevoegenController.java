package userinterface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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
    @FXML private TextField sbUserNaamtext;

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
        sbLeerlingGIDLabel.setVisible(userComboValue.equals      ("leerling"));
        sbLeerlingGID.setVisible(userComboValue.equals           ("leerling"));
        sbLeerlingKlas.setVisible(userComboValue.equals          ("leerling"));

        sbMedewerkerGIDLabel.setVisible(userComboValue.equals  ("medewerker"));
        sbMedewerkerGID.setVisible(userComboValue.equals       ("medewerker"));
        sbMedewerkerType.setVisible(userComboValue.equals      ("medewerker"));
    }
    public void action2(ActionEvent actionEvent){
        String medwerkerComboValue = (String) sbMedewerkerType.getValue();
        sbSLBMEDIDLabel.setVisible(medwerkerComboValue.equals        ("slb"));
        sbSLBMedID.setVisible(medwerkerComboValue.equals             ("slb"));
        sbSLBKlas.setVisible(medwerkerComboValue.equals              ("slb"));

        sbDocentMEDIDLabel.setVisible(medwerkerComboValue.equals  ("docent"));
        sbDocentMedID.setVisible(medwerkerComboValue.equals       ("docent"));
        sbDocentCursus.setVisible(medwerkerComboValue.equals      ("docent"));
    }
    public void opslaan(ActionEvent actionEvent){
        String gebruikersnaam = sbUserNaamtext.getText();
        String gebruikernaam = sbNaamtext.getText();
        String gebruikerWachtword = sbNaamtext.getText();
        String gebruikerType = (String) sbUsertype.getValue();
        String gebruikerID = "";
        if (gebruikerType.equals("leerling")){
            String klasID      = "";
            gebruikerID = gebruikerID(gebruikersnaam,gebruikernaam,gebruikerType,gebruikerWachtword);
            executeStatement("INSERT INTO leerling(gebruikerID,klasID) VALUES ('"+ gebruikerID + "', '" + klasID + "');");

        }

        if (gebruikerType.equals("medewerker")){
            gebruikerID(gebruikersnaam,gebruikernaam,gebruikerType,gebruikerWachtword);
            }

    }


    public static String gebruikerID(String a, String b, String c, String d){
        String gebruikerID  ="";
        executeStatement("INSERT INTO gebruiker(gebruikersnaam, naam, type, wachtwoord) VALUES ('"+ a + "', '" + b + "', '" + c + "', '" + d + "');");
        for (Map<String, Object> data : executeStatement("SELECT gebruikerID FROM gebruiker WHERE = gebruikersnaam" + a + ";")){
           gebruikerID = (String) data.get("gebruikerID");
        }
        return gebruikerID;
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

