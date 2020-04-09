package userinterface;

import model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.lang.reflect.AnnotatedArrayType;
import java.sql.DatabaseMetaData;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AfwezigeStudentenShowController {
    private LocalDate Datum;
    private String Les;
    private String KlasNaam;
    private List<Rooster> allelessen = new ArrayList<Rooster>();
    ArrayList<Map<String, Object>> Alleleerlingen;
    ArrayList<Map<String, Object>> Afwezigeleerlingen;
    ObservableList Aanwezig = FXCollections.observableArrayList();
    ObservableList Afwezig = FXCollections.observableArrayList();
    @FXML ListView AfwezigLijst;
    @FXML ListView AanwezigLijst;

    public void initialize(){
        AfwezigLijst.setItems(Afwezig);
        AanwezigLijst.setItems(Aanwezig);
    }

    public void SluitenMainFrame(ActionEvent actionEvent) {
        Button source = (Button)actionEvent.getSource();
        Stage stage = (Stage)source.getScene().getWindow();
        stage.close();
    }

    public void ChangeAbsentie(ActionEvent actionEvent) {
        System.out.println(Datum);
        System.out.println(Les);
        initialize();
        String[] Lesinfo = Les.split(":");
        String[] Splitter = Lesinfo[1].split("|");
        String LesCode = Splitter[0].strip();

        String[] lesminuten = Lesinfo[3].split("-");
        String begintijd = Lesinfo[2].strip() + ":" + lesminuten[0].strip() + ":00";

        String eindtijd = lesminuten[1].strip() + ":" + Lesinfo[4].strip() + ":00";
        int BegintijdUur = Integer.parseInt(Lesinfo[2].strip());
        int BegintijdMinuten = Integer.parseInt(lesminuten[0].strip());
        int EindtijdUur = Integer.parseInt(lesminuten[1].strip());
        int EindtijdMinuten = Integer.parseInt(Lesinfo[4].strip());
        LocalDateTime Begintijd = Datum.atTime(BegintijdUur, BegintijdMinuten);
        LocalDateTime Eindtijd = Datum.atTime(EindtijdUur, EindtijdMinuten);
        try {
            String naam = (String) AfwezigLijst.getSelectionModel().getSelectedItem();
            if(naam == null||naam.isEmpty()){
                naam = (String) AanwezigLijst.getSelectionModel().getSelectedItem();
                if(naam.equals(null)||naam.isEmpty()){
                    System.out.println("Er is niks geselecteerd");
                }
                else{
                    Utils.Database.executeStatement("DELETE FROM `afwezigheid` WHERE leerlingID = " + Utils.Database.executeStatement("SELECT leerlingID FROM leerling l, gebruiker g WHERE l.gebruikerID = g.gebruikerID AND g.naam = " + naam ));
                    Aanwezig.remove(naam);
                    Afwezig.add(naam);
                    initialize();
                }
            }else{
                Utils.Database.executeStatement("INSERT INTO `afwezigheid`(`reden`, `leerlingID`, `lesID`) VALUES ('Absent', '" + (Utils.Database.executeStatement("SELECT leerlingID FROM leerling l, gebruiker g WHERE l.gebruikerID = g.gebruikerID AND g.naam = " + naam )) + "', '" + Utils.Database.executeStatement("SELECT lesID FROM les, cursus WHERE begintijd = " + Begintijd +" AND eindtijd = " + Eindtijd + " AND les.cursusID = cursus.cursusID AND cursus.cursusNaam = " + LesCode) + "'");
                Aanwezig.add(naam);
                Afwezig.remove(naam);
                initialize();
            }
        }catch(Exception e){
            System.out.println("Kan actie niet uitvoeren");
            System.out.println(e);
            initialize();
        }
    }

    public void ClearViewList(){
        AanwezigLijst.getItems().clear();
        AfwezigLijst.getItems().clear();
    }

//    public void LijstMaken(){
//        System.out.println(getDate());
//        System.out.println(getles());
//        String[] Lesinfo = Les.split(":");
//        String[] Splitter = Lesinfo[1].split("|");
//        String LesCode = Splitter[0].strip();
//
//        String[] lesminuten = Lesinfo[3].split("-");
//
//        int BegintijdUur = Integer.parseInt(Lesinfo[2].strip());
//        int BegintijdMinuten = Integer.parseInt(lesminuten[0].strip());
//        int EindtijdUur = Integer.parseInt(lesminuten[1].strip());
//        int EindtijdMinuten = Integer.parseInt(Lesinfo[4].strip());
//        LocalDateTime Begintijd = Datum.atTime(BegintijdUur, BegintijdMinuten);
//        LocalDateTime Eindtijd = Datum.atTime(EindtijdUur, EindtijdMinuten);
//        allelessen = School.getSchool().getRooster();
//        for (int i = 0; i < allelessen.size(); i++){
//            if(allelessen.get(i).getlesdagDatum().equals(Datum) && allelessen.get(i).getlestijd().equals(""+Lesinfo[2].strip()+":"+Lesinfo[3]+":"+Lesinfo[4].strip()) && allelessen.get(i).getLes().equals(LesCode)){
//                KlasNaam = allelessen.get(i).getKlas().getNaam();
//            }
//        }
//        Alleleerlingen = Utils.Database.executeStatement("SELECT g.naam FROM gebruiker g INNER JOIN leerling l ON g.gebruikerID = l.gebruikerID INNER JOIN klas k ON l.klasID = k.klasID INNER JOIN les ON les.klasID = k.klasID INNER JOIN cursus c ON c.cursusID = les.cursusID WHERE les.begintijd = " + Begintijd + " AND k.klasNaam = " + KlasNaam);
//        for(int i = 0; i < Alleleerlingen.size(); i++){
//            Aanwezig.add(Alleleerlingen.get(i));
//        }
//    }

    public void Les(String LesNaam){
        Les = LesNaam;
    }

    public String getles(){
        return Les;
    }

    public void giveDatum(LocalDate TheDate){
        Datum = TheDate;
    }

    public LocalDate getDate(){
        return Datum;
    }

    public void setInformation() {
        String[] Lesinfo = Les.split(":");
        String[] Splitter = Lesinfo[1].split("|");
        String LesCode = Splitter[0].strip();

        String[] lesminuten = Lesinfo[3].split("-");

        int BegintijdUur = Integer.parseInt(Lesinfo[2].strip());
        int BegintijdMinuten = Integer.parseInt(lesminuten[0].strip());
        int EindtijdUur = Integer.parseInt(lesminuten[1].strip());
        int EindtijdMinuten = Integer.parseInt(Lesinfo[4].strip());
        LocalDateTime Begintijd = Datum.atTime(BegintijdUur, BegintijdMinuten);
        LocalDateTime Eindtijd = Datum.atTime(EindtijdUur, EindtijdMinuten);
        allelessen = School.getSchool().getRooster();
        for (int i = 0; i < allelessen.size(); i++){
            if(allelessen.get(i).getlesdagDatum().equals(Datum) && allelessen.get(i).getlestijd().equals(""+Lesinfo[2].strip()+":"+Lesinfo[3]+":"+Lesinfo[4].strip()) && allelessen.get(i).getLes().equals(LesCode)){
                KlasNaam = "TICT-SD-V1C"; //allelessen.get(i).getKlas().getNaam();
            }
        }
        Alleleerlingen = Utils.Database.executeStatement("SELECT g.naam FROM gebruiker g INNER JOIN leerling l ON g.gebruikerID = l.gebruikerID INNER JOIN klas k ON l.klasID = k.klasID INNER JOIN les ON les.klasID = k.klasID INNER JOIN cursus c ON c.cursusID = les.cursusID WHERE les.begintijd = '" + Begintijd + "' AND k.klasNaam = 'TICT-SD-V1C'");
        for(int i = 0; i < Alleleerlingen.size(); i++){
            Aanwezig.add(Alleleerlingen.get(i));
        }
        initialize();
    }
}