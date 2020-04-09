package userinterface;

import model.*;
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
    private String KlasNaam;
    ArrayList<Map<String, Object>> Alleleerlingen;
    private List<Rooster> allelessen = new ArrayList<Rooster>();
    ObservableList Aanwezig = FXCollections.observableArrayList();
    ObservableList Afwezig = FXCollections.observableArrayList();
    @FXML ListView AfwezigLijst;
    @FXML ListView AanwezigLijst;
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
                Utils.Database.executeStatement("DELETE FROM `afwezigheid` WHERE leerlingID = " + leerlingIDs.get(naam) + " and lesID = " + lesID);
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
        String[] Lesinfo = Les.split(":");
        String[] Splitter = Lesinfo[1].split("|");
        String LesCode = Splitter[0].strip();

        String[] lesminuten = Lesinfo[3].split("-");

        int BegintijdUur = Integer.parseInt(Lesinfo[2].strip());
        int BegintijdMinuten = Integer.parseInt(lesminuten[0].strip());
//        int EindtijdUur = Integer.parseInt(lesminuten[1].strip());
//        int EindtijdMinuten = Integer.parseInt(Lesinfo[4].strip());
        LocalDateTime Begintijd = Datum.atTime(BegintijdUur, BegintijdMinuten);
//        LocalDateTime Eindtijd = Datum.atTime(EindtijdUur, EindtijdMinuten);
        allelessen = School.getSchool().getRooster();
        for (int i = 0; i < allelessen.size(); i++){
            if(allelessen.get(i).getlesdagDatum().equals(Datum) && allelessen.get(i).getlestijd().equals(""+Lesinfo[2].strip()+":"+Lesinfo[3]+":"+Lesinfo[4].strip()) && allelessen.get(i).getLes().equals(LesCode)){
                KlasNaam = allelessen.get(i).getKlas().getNaam(); //
            }
        }
        Alleleerlingen = Utils.Database.executeStatement("SELECT g.naam, l.leerlingID, a.reden FROM gebruiker g INNER JOIN leerling l ON g.gebruikerID = l.gebruikerID INNER JOIN klas k ON l.klasID = k.klasID INNER JOIN les ON les.klasID = k.klasID LEFT OUTER JOIN afwezigheid a ON l.leerlingID = a.leerlingID WHERE les.lesID = " + lesID);
        for(int i = 0; i < Alleleerlingen.size(); i++){
            if (Alleleerlingen.get(i).get("reden") == null) {
                Aanwezig.add(Alleleerlingen.get(i).get("naam"));
                System.out.println(Alleleerlingen.get(i).get("naam"));
                System.out.println(Alleleerlingen.get(i).get("leerlingID"));
                System.out.println("Aanwezig? " + Alleleerlingen.get(i).get("reden"));
            } else {
                Afwezig.add(Alleleerlingen.get(i).get("naam"));
            }

            leerlingIDs.put((String) Alleleerlingen.get(i).get("naam"), (int) Alleleerlingen.get(i).get("leerlingID"));
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