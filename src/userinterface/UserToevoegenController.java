package userinterface;

import Utils.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.Map;

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

    @FXML private ComboBox<String> sbUsertype;
    @FXML private ComboBox<String> sbLeerlingKlas;
    @FXML private ComboBox<String> sbMedewerkerType;
    @FXML private ComboBox<String> sbSLBKlas;
    @FXML private ComboBox<String> sbDocentCursus;

    public void initialize() {
        try {
            ObservableList<String> usertype = FXCollections.observableArrayList("leerling","medewerker");
            sbUsertype.setItems(usertype);

            ObservableList<String> medewerkertype = FXCollections.observableArrayList("docent", "slb","decaan","beheerder");
            sbMedewerkerType.setItems(medewerkertype);

            ObservableList<String> klassen = FXCollections.observableArrayList();
            ObservableList<String> cursus = FXCollections.observableArrayList();

            String query = "SELECT klas.klasNaam, cursus.cursusNaam " +
                    "FROM klas " +
                    "LEFT OUTER JOIN cursus ON klas.klasID = cursus.klasID " +
                    "UNION " +
                    "SELECT klas.klasNaam, cursus.cursusNaam " +
                    "FROM klas " +
                    "RIGHT OUTER JOIN cursus ON klas.klasID = cursus.klasID";
            for (Map<String, Object> data : Database.executeStatement(query)){
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

    public void action() {
        String userComboValue = sbUsertype.getValue();
        sbLeerlingGIDLabel.setVisible(userComboValue.equals      ("leerling"));
        sbLeerlingGID.setVisible(userComboValue.equals           ("leerling"));
        sbLeerlingKlas.setVisible(userComboValue.equals          ("leerling"));

        sbMedewerkerGIDLabel.setVisible(userComboValue.equals  ("medewerker"));
        sbMedewerkerGID.setVisible(userComboValue.equals       ("medewerker"));
        sbMedewerkerType.setVisible(userComboValue.equals      ("medewerker"));
    }
    public void action2(){
        String medwerkerComboValue = sbMedewerkerType.getValue();
        sbSLBMEDIDLabel.setVisible(medwerkerComboValue.equals        ("slb"));
        sbSLBMedID.setVisible(medwerkerComboValue.equals             ("slb"));
        sbSLBKlas.setVisible(medwerkerComboValue.equals              ("slb"));

        sbDocentMEDIDLabel.setVisible(medwerkerComboValue.equals  ("docent"));
        sbDocentMedID.setVisible(medwerkerComboValue.equals       ("docent"));
        sbDocentCursus.setVisible(medwerkerComboValue.equals      ("docent"));
    }
    public void opslaan(){
        errorLabel.setText("");
        String gebruikersnaam = sbUserNaamtext.getText();
        String gebruikernaam = sbNaamtext.getText();
        String gebruikerWachtword = sbWachtword.getText();
        String gebruikerType = sbUsertype.getValue();

        checkTextFields(gebruikersnaam, gebruikernaam, gebruikerType, gebruikerWachtword);
        if (errorLabel.getText().isEmpty()) {
            if (gebruikerType.equals("leerling")) {
                int gebruikerID;
                int klasID = 0;
                gebruikerID = gebruikerID(gebruikersnaam, gebruikernaam, gebruikerType, gebruikerWachtword);
                String query = "SELECT klasID " +
                        "FROM klas " +
                        "WHERE klasNaam = '" + sbLeerlingKlas.getValue() + "'";
                for (Map<String, Object> data : Database.executeStatement(query))
                    klasID = (int) data.get("klasID");
                query = "INSERT INTO leerling " +
                        "(gebruikerID, klasID) VALUES " +
                        "(" + gebruikerID + ", " + klasID + ")";
                Database.executeStatement(query);
            }

            if (gebruikerType.equals("medewerker")) {
                int gebruikerID;
                String medewerkertype;
                medewerkertype = sbMedewerkerType.getValue();
                gebruikerID = gebruikerID(gebruikersnaam, gebruikernaam, gebruikerType, gebruikerWachtword);
                String query = "INSERT INTO medewerker " +
                        "(gebruikerID, medewerkerType) VALUES " +
                        "(" + gebruikerID + ", '" + medewerkertype + "')";
                Database.executeStatement(query);

                if (medewerkertype.equals("slb")) {
                    int medewerkerID = 0;
                    int klasID = 0;
                    query = "SELECT medewerkerID " +
                            "FROM medewerker " +
                            "WHERE gebruikerID = " + gebruikerID;
                    for (Map<String, Object> data : Database.executeStatement(query))
                        medewerkerID = (int) data.get("medewerkerID");
                    query = "SELECT klasID " +
                            "FROM klas " +
                            "WHERE klasNaam = '" + sbSLBKlas.getValue() + "';";
                    for (Map<String, Object> data : Database.executeStatement(query))
                        klasID = (int) data.get("klasID");
                    query = "INSERT INTO medewerkertoegangklas " +
                            "(medewerkerID, klasID) VALUES " +
                            "(" + medewerkerID + ", " + klasID + ")";
                    Database.executeStatement(query);
                }

                if (medewerkertype.equals("docent")) {
                    int medewerkerID = 0;
                    int cursusID = 0;
                    query = "SELECT medewerkerID " +
                            "FROM medewerker " +
                            "WHERE gebruikerID = " + gebruikerID;
                    for (Map<String, Object> data : Database.executeStatement(query))
                        medewerkerID = (int) data.get("medewerkerID");
                    query = "SELECT cursusID " +
                            "FROM cursus " +
                            "WHERE cursusNaam = '" + sbSLBKlas.getValue() + "'";
                    for (Map<String, Object> data : Database.executeStatement(query))
                        cursusID = (int) data.get("cursusID");
                    query = "INSERT INTO medewerkertoegangcursus " +
                            "(medewerkerID, cursusID) VALUES " +
                            "(" + medewerkerID + ", " + cursusID + ")";
                    Database.executeStatement(query);
                }

            }

            leegmaken();
            errorLabel.setText("De nieuwe " + gebruikerType + " is opgeslagen");
        }
    }

    public int gebruikerID(String a, String b, String c, String d){
        int gebruikerID = 0;
        String query = "INSERT INTO gebruiker " +
                "(gebruikersnaam, naam, gebruikerType, wachtwoord) VALUES " +
                "('"+ a + "', '" + b + "', '" + c + "', '" + d + "');";
        Database.executeStatement(query);
        query = "SELECT gebruikerID " +
                "FROM gebruiker " +
                "WHERE gebruikersnaam = '" + a + "';";
        for (Map<String, Object> data : Database.executeStatement(query)){
            gebruikerID = (int) data.get("gebruikerID");
        }
        return gebruikerID;
    }

    public void checkTextFields(String a, String b, String c, String d){
        boolean isUserTypeComboBoxEmpty = (sbUsertype.getSelectionModel().isEmpty());
        boolean aa = (a==null);
        boolean bb = (b==null);
        boolean cc = (c==null);
        String query = "SELECT gebruikersnaam " +
                "FROM gebruiker " +
                "WHERE gebruikersnaam = '" + a +"';";
        for (Map<String, Object> data : Database.executeStatement(query)) {
            System.out.println(data.get("gebruikersnaam"));
            if (a.equals(data.get("gebruikersnaam"))) {
                errorLabel.setText("gebruikersnaam bestaat al !");
            }
        }
        if (aa || bb || cc || isUserTypeComboBoxEmpty ){
                errorLabel.setText("vul alles in AUB !");
            }
    }

    public void reset(){
        leegmaken();
    }

    private void leegmaken() {
        sbUserNaamtext.setText("");
        sbDocentCursus.setValue("Kies een cursus");
        sbSLBKlas.setValue("Kies een Klas");
        sbMedewerkerType.setValue("Kies Medewerker Type");
        sbLeerlingKlas.setValue("Kies een Klas");
        sbUsertype.setValue("Kies User Type");
        sbMedewerkerGID.setText(null);
        sbLeerlingGID.setText(null);
        sbSLBMedID.setText(null);
        sbDocentMedID.setText(null);
        sbNaamtext.setText(null);
        sbWachtword.setText(null);
    }
}

