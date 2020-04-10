package userinterface;

import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.*;

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

    public void initialize(){
        AfwezigLijst.setItems(Afwezig);
        AanwezigLijst.setItems(Aanwezig);
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
                Utils.Database.executeStatement(query);
                Aanwezig.add(naam);
                Afwezig.remove(naam);
                initialize();
            }
        }catch(Exception e) {
            System.out.println("Kan actie niet uitvoeren");
            e.printStackTrace();
            initialize();

//        naam = (String) AfwezigLijst.getSelectionModel().getSelectedItem();
//        int leerlingID = leerlingIDs.get(naam);
//        if(naam.equals(null)||naam.isEmpty()){
//            System.out.println("Er is niks geselecteerd");
//        }
//        else{
//            Utils.Database.executeStatement("DELETE FROM `afwezigheid` WHERE leerlingID = " + leerlingID + " and lesID = " + lesID);
//            Aanwezig.remove(naam);
//            Afwezig.add(naam);
//            initialize();
//        }
        }
    }

    public void afwezigMelden() {
        try {
            String naam = AanwezigLijst.getSelectionModel().getSelectedItem();
            if (naam != null && !naam.isEmpty()) {
                System.out.println("leerlingID: " + leerlingIDs.get(naam));
                System.out.println("lesID: " + lesID);
                Utils.Database.executeStatement("INSERT INTO `afwezigheid` (`reden`, `leerlingID`, `lesID`) VALUES ('Absent', '" + leerlingIDs.get(naam) + "', '" + lesID + "')");
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
        String query = "SELECT g.naam, l.leerlingID, a.reden " +
                "FROM gebruiker g " +
                "INNER JOIN leerling l ON g.gebruikerID = l.gebruikerID " +
                "INNER JOIN klas k ON l.klasID = k.klasID " +
                "INNER JOIN les ON les.klasID = k.klasID " +
                "LEFT OUTER JOIN afwezigheid a ON l.leerlingID = a.leerlingID " +
                "WHERE les.lesID = " + lesID;
        Alleleerlingen = Utils.Database.executeStatement(query);
        for (Map<String, Object> stringObjectMap : Alleleerlingen) {
            if (stringObjectMap.get("reden") == null) {
                Aanwezig.add((String) stringObjectMap.get("naam"));
                System.out.println(stringObjectMap.get("naam"));
                System.out.println(stringObjectMap.get("leerlingID"));
                System.out.println("Afwezig? " + stringObjectMap.get("reden"));
            } else {
                Afwezig.add((String) stringObjectMap.get("naam"));
            }

            leerlingIDs.put((String) stringObjectMap.get("naam"), (int) stringObjectMap.get("leerlingID"));
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
}