package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class AfwezigeStudentenShowController {
    @FXML ListView AfwezigLijst;
    @FXML ListView AanwezigLijst;
    public void initialize(){
        
    }

    public void SluitenMainFrame(ActionEvent actionEvent) {
        Button source = (Button)actionEvent.getSource();
        Stage stage = (Stage)source.getScene().getWindow();
        stage.close();
    }

    public void ChangeAbsentie(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AfwezigheidVeranderen.fxml"));
            Parent root = loader.load();
            AfwezigheidVeranderenController controller = loader.getController();

            Stage newStage = new Stage();
            newStage.setTitle("Verander aanwezigheid");
            newStage.setScene(new Scene(root));
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.showAndWait();
            initialize();
        }catch(Exception e){
            System.out.println("Kan scherm niet openen");
        }
    }
    private void initialize() {
        System.out.println("test");
    }
}
