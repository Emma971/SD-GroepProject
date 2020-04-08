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
import java.util.ArrayList;
import java.util.Map;

public class AfwezigeStudentenShowController {
    String Les;
    ArrayList<Map<String, Object>> leerlingen;
    ObservableList Aanwezig = FXCollections.observableArrayList();
    ObservableList Afwezig = FXCollections.observableArrayList();
    @FXML ListView AfwezigLijst;
    @FXML ListView AanwezigLijst;

    public void initialize(){
        LijstMaken();
        AfwezigLijst.setItems(Afwezig);
        AanwezigLijst.setItems(Aanwezig);
    }

    public void SluitenMainFrame(ActionEvent actionEvent) {
        Button source = (Button)actionEvent.getSource();
        Stage stage = (Stage)source.getScene().getWindow();
        stage.close();
    }

    public void ChangeAbsentie(ActionEvent actionEvent) {
        initialize();
        try {
            String naam = (String) AfwezigLijst.getSelectionModel().getSelectedItem();
            if(naam == null||naam.isEmpty()){
                naam = (String) AanwezigLijst.getSelectionModel().getSelectedItem();
                if(naam.equals(null)||naam.isEmpty()){
                    System.out.println("Er is niks geselecteerd");
                }
                else{
                    System.out.println();
                    Aanwezig.remove(naam);
                    Afwezig.add(naam);
                    initialize();
                }
            }else{
                Aanwezig.add(naam);
                Afwezig.remove(naam);
                initialize();
            }
        }catch(Exception e){
            System.out.println("Kan actie niet uitvoeren");
            initialize();
        }
    }

    public void ClearViewList(){
        AanwezigLijst.getItems().clear();
        AfwezigLijst.getItems().clear();
    }

    public void LijstMaken(){
        if(Aanwezig.isEmpty() && Afwezig.isEmpty()) {
            Afwezig.add("Piet van Hoekstra");
            Afwezig.add("Peter Allemans");
            Aanwezig.add("Jan van der Steen");
            Aanwezig.add("Ronald van der Laan");
            Aanwezig.add("Steven Chez");
            Aanwezig.add("Sam Nijkant");
        }
    }

    public void Les(String LesNaam){
        Les = LesNaam;
    }
}
