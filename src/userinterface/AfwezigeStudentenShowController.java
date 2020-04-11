package userinterface;

import Utils.Gebruiker;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.*;

import static Utils.Database.executeStatement;

public class AfwezigeStudentenShowController {
    private LocalDate Datum;
    private String Les;
    ArrayList<Map<String, Object>> Alleleerlingen;
    ObservableList<String> Aanwezig = FXCollections.observableArrayList();
    ObservableList<String> Afwezig = FXCollections.observableArrayList();
    @FXML ListView<String> AfwezigLijst;
    @FXML ListView<String> AanwezigLijst;
    @FXML Button Afwezigbutton;
    @FXML Button Aanwezigbutton;
    private int lesID;
    private HashMap<String, Integer> leerlingIDs = new HashMap<>();
    private Gebruiker gebruiker;

    public void initialize(){
        AanwezigLijst.setItems(Aanwezig);
        AfwezigLijst.setItems(Afwezig);
    }

    public void SluitenMainFrame(ActionEvent actionEvent) {
        Button source = (Button)actionEvent.getSource();
        Stage stage = (Stage)source.getScene().getWindow();
        stage.close();
    }

    public void aanwezigMelden() {
        System.out.println(Datum);
        System.out.println(Les);
        try {
            String naam = AfwezigLijst.getSelectionModel().getSelectedItem();
            if (naam != null && !naam.isEmpty()) {
                System.out.println("leerlingID: " + leerlingIDs.get(naam));
                System.out.println("lesID: " + lesID);
                String query = "DELETE FROM `afwezigheid` " +
                        "WHERE leerlingID = " + leerlingIDs.get(naam) + " " +
                        "AND lesID = " + lesID;
                executeStatement(query);
                Aanwezig.add(naam);
                Afwezig.remove(naam);
                initialize();
            }
        }catch(Exception e) {
            System.out.println("Kan actie niet uitvoeren");
            e.printStackTrace();
            initialize();
        }
    }

    public void afwezigMelden() {
        try {
            String naam = AanwezigLijst.getSelectionModel().getSelectedItem();
            if (naam != null && !naam.isEmpty()) {
                System.out.println("leerlingID: " + leerlingIDs.get(naam));
                System.out.println("lesID: " + lesID);
                executeStatement("INSERT INTO `afwezigheid` (`reden`, `leerlingID`, `lesID`) VALUES ('Absent', '" + leerlingIDs.get(naam) + "', '" + lesID + "')");
                Afwezig.add(naam);
                Aanwezig.remove(naam);
                initialize();
            }
        }catch(Exception e){
            System.out.println("Kan actie niet uitvoeren");
            e.printStackTrace();
            initialize();
        }
    }

    public void LijstMaken(){
        System.out.println(lesID);
        Aanwezig.add("Naam - LeerlingNummer");
        Afwezig.add("Naam - LeerlingNummer");
        String query = "SELECT gebruiker.naam, leerling.leerlingID " +
                "FROM gebruiker " +
                "INNER JOIN leerling ON gebruiker.gebruikerID = leerling.gebruikerID " +
                "INNER JOIN klas ON leerling.klasID = klas.klasID " +
                "INNER JOIN les ON les.klasID = klas.klasID " +
                "WHERE les.lesID = " + lesID + " " +
                "ORDER BY gebruiker.naam; ";
        for (Map<String, Object> data : executeStatement(query)) {
            String aanwezigestudenten = data.get("naam") + " - " + data.get("leerlingID");
            if (!Aanwezig.contains(aanwezigestudenten))
                Aanwezig.add(aanwezigestudenten);
        }
        query = "SELECT gebruiker.naam, leerling.leerlingID " +
                "FROM afwezigheid " +
                "INNER JOIN leerling ON afwezigheid.leerlingID = leerling.leerlingID " +
                "INNER JOIN gebruiker ON leerling.gebruikerID = gebruiker.gebruikerID " +
                "INNER JOIN klas ON leerling.klasID = klas.klasID " +
                "INNER JOIN les ON klas.klasID = les.klasID " +
                "WHERE afwezigheid.lesID = " + lesID + " " +
                "ORDER BY gebruiker.naam; ";
        for (Map<String, Object> afw : executeStatement(query)) {
            String afwezigestudenten = afw.get("naam") + " - " + afw.get("leerlingID");
            System.out.println(afwezigestudenten);
            if (!Afwezig.contains(afwezigestudenten))
                Afwezig.add(afwezigestudenten);

            if (Aanwezig.contains(afwezigestudenten))
                Aanwezig.remove(afwezigestudenten);

        AanwezigLijst.setItems(Aanwezig);
        AfwezigLijst.setItems(Afwezig);
    }
    }

    public void setLesID(int lesID) {
        this.lesID = lesID;
    }

    public void Les(String LesNaam){
        Les = LesNaam;
    }

    public void giveDatum(LocalDate TheDate){
        Datum = TheDate;
    }

    public void zetAlleleerlingen(){
        LijstMaken();
        initialize();
    }

    public void zetButtonsVisable(String usertype){
            Afwezigbutton.setVisible      (usertype.equals("docent"));
            Aanwezigbutton.setVisible      (usertype.equals("docent"));
    }

    public void setGebruiker(Gebruiker gebruiker) {
        this.gebruiker = gebruiker;
    }
}