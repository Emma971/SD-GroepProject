package UserInterfaces;

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

public class AfwezigeStudentenShowController {
    ObservableList Aanwezig = FXCollections.observableArrayList();
    ObservableList Afwezig = FXCollections.observableArrayList();
    @FXML ListView AfwezigLijst;
    @FXML ListView AanwezigLijst;


    public void initialize(){
        Aanwezig.add("Piet Huizen");
        Aanwezig.add("Jan Steenhuis");
        Afwezig.add("Ronald van der Steen");
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
            if(naam.equals(null)||naam.isEmpty()){
                naam = (String) AanwezigLijst.getSelectionModel().getSelectedItem();
                if(naam.equals(null)||naam.isEmpty()){
                    ClearViewList();
                    System.out.println("Er is niks geselecteerd");
                }
                else{
                    ClearViewList();
                    Aanwezig.remove(naam);
                    Afwezig.add(naam);
                    initialize();
                }
            }else{
                ClearViewList();
                Aanwezig.add(naam);
                Afwezig.remove(naam);
                initialize();
            }
        }catch(Exception e){
            ClearViewList();
            System.out.println("Kan actie niet uitvoeren");
            initialize();
        }
    }

    public void ClearViewList(){
        AanwezigLijst.getItems().clear();
        AfwezigLijst.getItems().clear();
    }
}
