package userinterface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

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


    public AfmeldenLesController() {}

    public void initialize() {
        datePicker.setValue(LocalDate.now());
        setVakTijdComboBox();
    }

    public void BevestigAfmelding(ActionEvent actionEvent) {

    }

    public void setVakTijdComboBox() {
        ObservableList<String> lessen = FXCollections.observableArrayList();
        date = datePicker.getValue();

        for (Rooster rooster : school.getRooster()) {
            if (rooster.getlesdagDatum().isEqual(date)) {
                lessen.add("Les: " +rooster.getLes() + "Klas: " +rooster.getKlas()+ "Tijd: " + rooster.getlestijd());
            }
        } vakTijdComboBox.setItems(lessen);
    }

    public ToggleGroup getRedenAfmelding() {
        return redenAfmelding;
    }

    public ToggleGroup getLengteAfmelding() {
        return lengteAfmelding;
    }

    public DatePicker getDatePicker() {
        return datePicker;
    }

    public ComboBox getVakTijdComboBox() {
        return vakTijdComboBox;
    }

    public DatePicker getAbsentEndDatePicker() {
        return AbsentEndDatePicker;
    }

    public DatePicker getAbsentStartDatePicker() {
        return AbsentStartDatePicker;
    }
}
