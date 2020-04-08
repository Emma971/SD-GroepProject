package userinterface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Rooster;
import model.School;

import javax.print.Doc;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DocentRoosterController {
    @FXML private Label dagLabel;
    @FXML private Label weekLabel;
    @FXML private Label errorLabel;
    @FXML private Label roosternaamLabel;

    @FXML private ListView dagRoosterListView;
    @FXML private ListView weekRoosterListView;

    @FXML private DatePicker overzichtDatePicker;

    private School school = School.getSchool();
    private String Lesnaam = "";

    public void initialize() {
        try {
            overzichtDatePicker.setValue(LocalDate.now());
            roosternaamLabel.setText("" + mainmenuController.getUsernaam());
            toonlessen();
        } catch (NullPointerException e) {
            errorLabel.setText("Error! couldn't load the data");
        }
    }

    public void toonVanDaag(ActionEvent actionEvent) {
        overzichtDatePicker.setValue(LocalDate.now());
    }

    public void toonVorigeDag(ActionEvent actionEvent) {
        LocalDate dagEerder = overzichtDatePicker.getValue().minusDays(1);
        overzichtDatePicker.setValue(dagEerder);
    }

    public void toonVolgendeDag(ActionEvent actionEvent) {
        LocalDate dagLater = overzichtDatePicker.getValue().plusDays(1);
        overzichtDatePicker.setValue(dagLater);
    }

    public void toonlessen() {
        try {
        ObservableList<String> dagles = FXCollections.observableArrayList();
        ObservableList<String> weekles = FXCollections.observableArrayList();

        LocalDate dagX = overzichtDatePicker.getValue();
        DateTimeFormatter forX = DateTimeFormatter.ofPattern("w");
        int weekX = Integer.parseInt(dagX.format(forX));

        dagLabel.setText("" + overzichtDatePicker.getValue().getDayOfWeek());
        weekLabel.setText("Week : " + weekX);
        for (Rooster x : school.getRooster()){
            if(overzichtDatePicker.getValue().isEqual(x.getlesdagDatum())){
                String lesinfo="";
                lesinfo = lesinfo + "Les : " + x.getLes();
                lesinfo = lesinfo + " | tijd : " + x.getlestijd();
                dagles.add(lesinfo);
            }

            if(weekX==x.getWeeknummer()){
                String lesinfo="";
                lesinfo = lesinfo + "" + x.getlesdagDatum().getDayOfWeek();
                lesinfo = lesinfo + " | Les : " + x.getLes();
                lesinfo = lesinfo + " | tijd : " + x.getlestijd();
                weekles.add(lesinfo);
            }
        }
        dagRoosterListView.setItems(dagles);
        weekRoosterListView.setItems(weekles);
        } catch (NullPointerException e) {
            errorLabel.setText("Error! couldn't load the data");
        }
    }

    public void Afwezigheid(ActionEvent actionEvent) {
        try {
            Lesnaam = (String) dagRoosterListView.getSelectionModel().getSelectedItem();
            System.out.println(Lesnaam);
            String fxmlPagina = "AfwezigeStudentenShowController.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPagina));
            Parent root = loader.load();

            AfwezigeStudentenShowController controller = loader.getController();
            controller.Les(Lesnaam);
            Stage stage = new Stage();
            if(Lesnaam.equals(null) || Lesnaam.isEmpty()){
                stage.setTitle("Absentie");
            }else {
                stage.setTitle(Lesnaam);
            }
            stage.setScene(new Scene(root));
            stage.show();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            errorLabel.setText("Error! can't access absent melden window ");
        }
    }
}