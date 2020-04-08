package userinterface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.Rooster;
import model.School;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static Utils.Database.executeStatement;

public class SchoolOverzichtController {
    @FXML private Label dagLabel;
    @FXML private Label weekLabel;
    @FXML private Label errorLabel;
    @FXML private Label roosternaamLabel;

    @FXML private ListView dagRoosterListView;
    @FXML private ListView weekRoosterListView;

    @FXML private DatePicker overzichtDatePicker;

    private School school = School.getSchool();

    public void initialize() {
        try {
            overzichtDatePicker.setValue(LocalDate.now());
            roosternaamLabel.setText("" + mainmenuController.getUsernaam() + " Klas : " + mainmenuController.getUserklasnaam());
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

    public void absentmelden(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("AfmeldenLes.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Absent Melden");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            errorLabel.setText("Error! can't access absent melden window ");
        }
    }
}