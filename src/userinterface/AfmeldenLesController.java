package userinterface;

import Utils.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static Utils.Database.executeStatement;

public class AfmeldenLesController {

    @FXML    /* Lengte afmelding */
    private ToggleGroup lengteAfmelding;
    @FXML
    private RadioButton dezeLesRadioButton;
    @FXML
    private RadioButton heleDagRadioButton;
    @FXML
    private RadioButton VanTotRadioButton;
    @FXML
    private DatePicker AbsentStartDatePicker;
    @FXML
    private DatePicker AbsentEndDatePicker;

    @FXML    /* Reden afmelding */
    private ToggleGroup redenAfmelding;
    @FXML
    private RadioButton geenRedenRadioButton;
    @FXML
    private RadioButton ziekRadioButton;
    @FXML
    private RadioButton dokterRadioButton;
    @FXML
    private RadioButton andersRadioButton;
    @FXML
    private TextArea andersTextArea;

    @FXML   /* De rest */
    private DatePicker datePicker;
    @FXML
    private ComboBox vakTijdComboBox;
    @FXML
    private Button bevestigButton;

    private School school = School.getSchool();
//    public Rooster rooster;
    private LocalDate date;
    private String reden;
    private int lesID;


    public AfmeldenLesController() {}

    public void initialize() {
        datePicker.setValue(LocalDate.now());
        setVakTijdComboBox();
    }

    public void BevestigAfmelding(ActionEvent actionEvent) {
        date = datePicker.getValue();
        String les = String.valueOf(vakTijdComboBox.getSelectionModel().getSelectedItem());
        RedenAfmelding();

        ObservableList<String> lessen = FXCollections.observableArrayList();
        ArrayList<Map<String, Object>> data = executeStatement("SELECT les.begintijd, les.eindtijd, les.lesID, cursus.cursusNaam  FROM les INNER JOIN cursus on les.cursusID = cursus.cursusID WHERE begintijd < '2020-04-09'AND begintijd > '2020-04-08';");

        for (Map<String, Object>less : data) {
            lessen.add("Les: " + less.get("cursusNaam") + " Tijd:" + less.get("begintijd") + " : " + less.get("eindtijd"));
            String lesles = "Les: " + less.get("cursusNaam") + " Tijd:" + less.get("begintijd") + " : " + less.get("eindtijd");
            lesID = (int) less.get("lesID");
            HashMap<String, Integer> = lesles, lesID;
        }


        int leerlingID = 5;
        int lesID = 8;
        executeStatement("INSERT INTO afwezigheid (reden, leerlingID, lesID) VALUES ('" + reden + "', '" + leerlingID + "', '" + lesID + "');");

    }

    public void setVakTijdComboBox() {
        ObservableList<String> lessen = FXCollections.observableArrayList();
        date = datePicker.getValue();

        ArrayList<Map<String, Object>> data = executeStatement("SELECT les.begintijd, les.eindtijd, cursus.cursusNaam  FROM les INNER JOIN cursus on les.cursusID = cursus.cursusID WHERE begintijd < '2020-04-09'AND begintijd > '2020-04-08';");


//        if (rooster.getlesdagDatum().isEqual(date)) {
//            lessen.add("Les: " +rooster.getLes() + "Tijd: " + rooster.getlestijd());
        for (Map<String, Object>les : data) {
            lessen.add("Les: " + les.get("cursusNaam") + " Tijd:" + les.get("begintijd") + " : " + les.get("eindtijd"));

        }
         vakTijdComboBox.setItems(lessen);
    }

    public void RedenAfmelding() {
        Toggle toggleReden = redenAfmelding.getSelectedToggle();
        if (toggleReden == geenRedenRadioButton) {
            reden = "Geen reden";
        } if (toggleReden == ziekRadioButton) {
            reden = "Ziek";
        } if (toggleReden == dokterRadioButton) {
            reden = "Dokter/Ortho/Tandarts";
        } if (toggleReden == andersRadioButton) {
            reden = String.valueOf(andersTextArea);
        }
    }

    public ToggleGroup getLengteAfmelding() {
        return lengteAfmelding;
    }

    public DatePicker getDatePicker() {
        return datePicker;
    }

    public DatePicker getAbsentEndDatePicker() {
        return AbsentEndDatePicker;
    }

    public DatePicker getAbsentStartDatePicker() {
        return AbsentStartDatePicker;
    }


}
