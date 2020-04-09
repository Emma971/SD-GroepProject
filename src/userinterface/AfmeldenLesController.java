package userinterface;

import Utils.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    private ComboBox<String> lesTijdComboBox;
    @FXML
    private Button bevestigButton;

    private School school = School.getSchool();
//    public Rooster rooster;
    private LocalDate date;
    private String reden;
    private int lesID;
    private SchoolOverzichtController parentController;
    private HashMap<String, Integer> lesIDs = new HashMap<>();

    public void setParentController(SchoolOverzichtController controller) {
        this.parentController = controller;
    }


    public AfmeldenLesController() {}

    public void initialize() {
        datePicker.setValue(LocalDate.now());
        setVakTijdComboBox();
    }

    public void BevestigAfmelding() {
        date = datePicker.getValue();
        String les = lesTijdComboBox.getSelectionModel().getSelectedItem();
        RedenAfmelding();

        int gebruikerID = parentController.parentController.getGebruikerID();


        int leerlingID = 5;
        int lesID = lesIDs.get(les);
        Database.executeStatement("INSERT INTO afwezigheid (reden, leerlingID, lesID) VALUES ('" + reden + "', '" + leerlingID + "', '" + lesID + "');");

    }

    public void setVakTijdComboBox() {
        int gebruikerID = parentController.parentController.getGebruikerID();
        date = datePicker.getValue();
        ArrayList<Map<String, Object>> data = executeStatement("SELECT les.begintijd, les.eindtijd, les.lesID, cursus.cursusNaam FROM les INNER JOIN cursus ON les.cursusID = cursus.cursusID INNER JOIN leerling ON les.klasID = leerling.klasID WHERE les.begintijd >= '" + date + " 00:00:00' AND les.begintijd <= '" + date + " 23:59:59' AND leerling.gebruikerID = " + gebruikerID + ";");
        ObservableList<String> lessen = FXCollections.observableArrayList();
        System.out.println(data);
        for (Map<String, Object> les : data) {
            String lesString ="Les: " + les.get("cursusNaam") + " Tijd:" + les.get("begintijd") + " : " + les.get("eindtijd");
            lesIDs.put(lesString, (int) les.get("lesID"));
            lessen.add(lesString);
            if (lesID == (int) les.get("lesID")) {
                valueToSelect = lesString;
            }
        }
         lesTijdComboBox.setItems(lessen);
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

    public void selectAnders() {
        redenAfmelding.selectToggle(andersRadioButton);
    }

    public void setDatePicker(LocalDate date) {
        datePicker.setValue(date);
    }
}
