package userinterface;

import Utils.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.Map;

public class UserToevoegenController {
    @FXML private Label errorLabel;

    @FXML private TextField sbNaamtext;
    @FXML private TextField sbWachtword;
    @FXML private TextField sbUserNaamtext;

    @FXML private ComboBox<String> sbUsertype;
    @FXML private ComboBox<String> sbLeerlingKlas;
    @FXML private ComboBox<String> sbLeerlingSLB;
    @FXML private ComboBox<String> sbMedewerkerType;
    @FXML private ComboBox<String> sbDocentCursus;
    @FXML private ComboBox<String> sbDecaan;

    public void initialize() {
        try {
            ObservableList<String> usertype = FXCollections.observableArrayList("leerling","medewerker");
            sbUsertype.setItems(usertype);

            ObservableList<String> medewerkertype = FXCollections.observableArrayList("docent", "slb","decaan","beheerder");
            sbMedewerkerType.setItems(medewerkertype);

            ObservableList<String> klassen = FXCollections.observableArrayList();
            ObservableList<String> cursus = FXCollections.observableArrayList();
            ObservableList<String> SLB = FXCollections.observableArrayList();

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
                    if(!klassen.contains(klasNaam))
                        klassen.add(klasNaam);
                }
                if(cursusnaam!=null){
                    cursus.add(cursusnaam);}
            }
            query = "SELECT gebruiker.naam " +
                    "FROM gebruiker " +
                    "INNER JOIN medewerker ON gebruiker.gebruikerID = medewerker.gebruikerID " +
                    "WHERE medewerkerType = 'slb' " +
                    "ORDER BY gebruiker.naam;";

            for (Map<String, Object> data : Database.executeStatement(query)){
                String slbnaam   = (String) data.get("naam");
                if(slbnaam!=null){
                    SLB.add(slbnaam);
                }
            }

            sbLeerlingSLB.setItems(SLB);
            sbLeerlingKlas.setItems(klassen);
            sbDecaan.setItems(klassen);
            sbDocentCursus.setItems(cursus);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    public void action() {
        String userComboValue = sbUsertype.getValue();
        sbLeerlingKlas.setVisible(userComboValue.equals      ("leerling"));
        sbLeerlingSLB.setVisible(userComboValue.equals       ("leerling"));
        sbMedewerkerType.setVisible(userComboValue.equals  ("medewerker"));
    }
    public void action2(){
        String medwerkerComboValue = sbMedewerkerType.getValue();
        sbDocentCursus.setVisible(medwerkerComboValue.equals   ("docent"));
        sbDecaan.setVisible(medwerkerComboValue.equals   ("docent"));
    }

    public void opslaan(){
        errorLabel.setText("");
        String gebruikersnaam     = sbUserNaamtext.getText();
        String gebruikernaam      = sbNaamtext.getText();
        String gebruikerWachtword = sbWachtword.getText();
        String gebruikerType      = sbUsertype.getValue();

        String cbleerlingklas     = sbLeerlingKlas.getValue();
        String cbleerlingSLB      = sbLeerlingSLB.getValue();
        String cbmedewerkertype   = sbMedewerkerType.getValue();
        String cbdocentcursus     = sbDocentCursus.getValue();

        checkBasicFields(gebruikersnaam, gebruikernaam, gebruikerWachtword);
        if (errorLabel.getText().isEmpty()) { // deze check is voor gebruikersData
            if (gebruikerType.equals("leerling")) {
                checkCombobox(cbleerlingklas);
                checkCombobox(cbleerlingSLB);
                if (errorLabel.getText().isEmpty()) { // deze check is voor de leerlingdata
                    int gebruikerID;
                    int klasID = 0;
                    int SLBID = 0;
                    gebruikerID = gebruikerID(gebruikersnaam, gebruikernaam, gebruikerType, gebruikerWachtword);
                    String query = "SELECT klasID " +
                            "FROM klas " +
                            "WHERE klasNaam = '" + sbLeerlingKlas.getValue() + "'";
                    for (Map<String, Object> data : Database.executeStatement(query))
                        klasID = (int) data.get("klasID");
                    query = "SELECT medewerkerID " +
                            "FROM medewerker " +
                            "INNER JOIN gebruiker ON medewerker.gebruikerID = gebruiker.gebruikerID "+
                            "WHERE naam = '" + sbLeerlingSLB.getValue() + "'";
                    for (Map<String, Object> data : Database.executeStatement(query))
                        SLBID = (int) data.get("medewerkerID");
                    query = "INSERT INTO leerling " +
                            "(gebruikerID, klasID, SLBID) VALUES " +
                            "(" + gebruikerID + ", " + klasID + ", " + SLBID +")";
                    Database.executeStatement(query);
                    errorLabel.setText("De nieuwe " + gebruikerType + " is opgeslagen");
                    leegmaken();
                }
            }

            if (gebruikerType.equals("medewerker")) {
                checkCombobox(cbmedewerkertype);
                if (errorLabel.getText().isEmpty()) {
                    int gebruikerID;
                    String medewerkertype;

                    medewerkertype = sbMedewerkerType.getValue();
                    gebruikerID = gebruikerID(gebruikersnaam, gebruikernaam, gebruikerType, gebruikerWachtword);
                    String query = "INSERT INTO medewerker " +
                            "(gebruikerID, medewerkerType) VALUES " +
                            "(" + gebruikerID + ", '" + medewerkertype + "')";
                    Database.executeStatement(query);

                    if (medewerkertype.equals("docent")) {
                        checkCombobox(cbdocentcursus);
                        if (errorLabel.getText().isEmpty()) {
                            int medewerkerID = 0;
                            int cursusID = 0;
                            query = "SELECT medewerkerID " +
                                    "FROM medewerker " +
                                    "WHERE gebruikerID = " + gebruikerID;
                            for (Map<String, Object> data : Database.executeStatement(query))
                                medewerkerID = (int) data.get("medewerkerID");
                            query = "SELECT cursusID " +
                                    "FROM cursus " +
                                    "WHERE cursusNaam = '" + sbDocentCursus.getValue() + "'";
                            for (Map<String, Object> data : Database.executeStatement(query))
                                cursusID = (int) data.get("cursusID");
                            query = "INSERT INTO medewerkertoegangcursus " +
                                    "(medewerkerID, cursusID) VALUES " +
                                    "(" + medewerkerID + ", " + cursusID + ")";
                            Database.executeStatement(query);
                        }
                    }
                    if (medewerkertype.equals("decaan")) {
                        checkCombobox(cbdocentcursus);
                        if (errorLabel.getText().isEmpty()) {
                            int medewerkerID = 0;
                            int klasID = 0;
                            query = "SELECT medewerkerID " +
                                    "FROM medewerker " +
                                    "WHERE gebruikerID = " + gebruikerID;
                            for (Map<String, Object> data : Database.executeStatement(query))
                                medewerkerID = (int) data.get("medewerkerID");
                            query = "SELECT klasID " +
                                    "FROM klas " +
                                    "WHERE klasNaam = '" + sbDecaan.getValue() + "'";
                            for (Map<String, Object> data : Database.executeStatement(query))
                                klasID = (int) data.get("klasID");
                            query = "INSERT INTO medewerkertoegangklas " +
                                    "(medewerkerID, klasID) VALUES " +
                                    "(" + medewerkerID + ", " + klasID + ")";
                            Database.executeStatement(query);
                        }
                    }
                    errorLabel.setText("De nieuwe " + gebruikerType + " is opgeslagen");
                    leegmaken();
                }
            }

        }
    }

    public int gebruikerID(String gebruikersnaam, String gebruikernaam, String gebruikerswachtwoord, String gebruikerstype){
        int gebruikerID = 0;
        String query = "INSERT INTO gebruiker " +
                "(gebruikersnaam, naam, gebruikerType, wachtwoord) VALUES " +
                "('"+ gebruikersnaam + "', '" + gebruikernaam + "', '" + gebruikerswachtwoord + "', '" + gebruikerstype + "');";
        Database.executeStatement(query);
        query = "SELECT gebruikerID " +
                "FROM gebruiker " +
                "WHERE gebruikersnaam = '" + gebruikersnaam + "';";
        for (Map<String, Object> data : Database.executeStatement(query)){
            gebruikerID = (int) data.get("gebruikerID");
        }
        return gebruikerID;
    }

    public void checkBasicFields(String gebruikersnaam, String gebruikernaam, String gebruikerswachtwoord){
        boolean isUserTypeComboBoxEmpty       = (sbUsertype.getSelectionModel().isEmpty());

        boolean gebruikersnaamCheck = (gebruikersnaam       ==null);
        boolean gebruikernaamCheck = (gebruikernaam        ==null);
        boolean gebruikerswachtwoordCheck = (gebruikerswachtwoord ==null);
        String query = "SELECT gebruikersnaam " +
                "FROM gebruiker " +
                "WHERE gebruikersnaam = '" + gebruikersnaam +"';";
        for (Map<String, Object> data : Database.executeStatement(query)) {
            System.out.println(data.get("gebruikersnaam"));
            if (gebruikersnaam.equals(data.get("gebruikersnaam"))) {
                errorLabel.setText("gebruikersnaam bestaat al !");
            }
        }
        if ((gebruikersnaamCheck || gebruikernaamCheck || gebruikerswachtwoordCheck || isUserTypeComboBoxEmpty)){
                errorLabel.setText("vul alles in AUB !");
            }
    }

    public void checkCombobox(String gebruikersCombo){
        boolean gebruikersComboCheck = ( gebruikersCombo==null );

        if ((gebruikersComboCheck)){
            errorLabel.setText("vul alles in AUB !");
        }
    }

    public void reset(){
        leegmaken();
    }

    private void leegmaken() {
        sbUserNaamtext.setText(null);
        sbNaamtext.setText(null);
        sbWachtword.setText(null);

        sbUsertype.setValue("Kies User Type");
        sbLeerlingKlas.setValue("Kies een Klas");
        sbLeerlingSLB.setValue("Kies een Klas");
        sbMedewerkerType.setValue("Kies Medewerker Type");
        sbDocentCursus.setValue("Kies een cursus");
    }
}

