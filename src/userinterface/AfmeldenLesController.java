package userinterface;

import Utils.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AfmeldenLesController {

    @FXML    /* Lengte afmelding */
    private ToggleGroup lengteAfmeldingToggle;
    @FXML
    private RadioButton dezeLesRadioButton;
    @FXML
    private RadioButton heleDagRadioButton;
    @FXML
    private RadioButton vanTotRadioButton;
    @FXML
    private DatePicker AbsentStartDatePicker;
    @FXML
    private DatePicker AbsentEndDatePicker;

    @FXML    /* Reden afmelding */
    private ToggleGroup redenAfmelding;
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

    private HashMap<String, Integer> lesIDs = new HashMap<>();
    private Gebruiker gebruiker;

    public void initialize() {
        datePicker.setValue(LocalDate.now());
    }

    public void BevestigAfmelding() {
        try {
            int leerlingID = gebruiker.getTypeID();

            // Duur van afwezigheid
            ArrayList<Integer> lesIDsAfwezig = new ArrayList<>();
            Toggle lengteAfmelding = lengteAfmeldingToggle.getSelectedToggle();
            System.out.println(lengteAfmelding);
            if (lengteAfmelding == dezeLesRadioButton) {
                Integer lesID = lesIDs.get(lesTijdComboBox.getSelectionModel().getSelectedItem());  // Integer kan null zijn, int niet
                if (lesID == null) {
                    throw new IllegalArgumentException("Geen les geselecteerd");
                }
                lesIDsAfwezig.add(lesID);
            } else if (lengteAfmelding == heleDagRadioButton) {
                lesIDsAfwezig = new ArrayList<>(lesIDs.values());
            } else if (lengteAfmelding == vanTotRadioButton) {
                LocalDate start = AbsentStartDatePicker.getValue();
                LocalDate end = AbsentEndDatePicker.getValue();
                if (start == null && end != null) {
                    Popup.alert("Begindatum niet geselecteerd");
                } else if (start != null && end == null) {
                    Popup.alert("Einddatum niet geselecteerd");
                } else if (start == null /* && end == null */) {
                    Popup.alert("Begin- en einddatum niet geselecteerd");
                } else {
                    String query = "SELECT les.lesID " +
                            "FROM les LEFT " +
                            "OUTER JOIN afwezigheid ON les.lesID = afwezigheid.lesID " +
                            "INNER JOIN cursus ON les.cursusID = cursus.cursusID " +
                            "INNER JOIN leerling ON les.klasID = leerling.klasID " +
                            "WHERE les.begintijd >= '" + start + " 00:00:00' " +
                            "AND les.begintijd <= '" + end + " 23:59:59' " +
                            "AND afwezigheid.leerlingID IS NULL " +
                            "AND leerling.leerlingID = " + leerlingID;
                    ArrayList<Map<String, Object>> lessen = Database.executeStatement(query);
                    System.out.println(lessen);
                    for (Map<String, Object> les : lessen) {
                        lesIDsAfwezig.add((int) les.get("lesID"));
                    }
                }

            }

            // Reden
            String reden;
            Toggle toggleReden = redenAfmelding.getSelectedToggle();
            if (toggleReden == ziekRadioButton) {
                reden = "Ziek";
            } else if (toggleReden == dokterRadioButton) {
                reden = "Dokter / Ortho / Tandarts";
            } else if (toggleReden == andersRadioButton) {
                reden = andersTextArea.getText();
            } else {
                throw new IllegalArgumentException("Geen reden opgegeven");
            }


            StringBuilder query = new StringBuilder("INSERT INTO afwezigheid " +
                    "(reden, leerlingID, lesID) VALUES ");
            for (Integer lesID : lesIDsAfwezig) {
                query.append( "('" ).append( reden ).append( "', '" ).append( leerlingID ).append( "', '" ).append( lesID ).append( "')," );
            }
            query.deleteCharAt(query.length() - 1);
//        Database.executeStatement(query.toString());
            System.out.println(query);
        } catch (IllegalArgumentException e) {
            Popup.alert(e.getLocalizedMessage());
        }
    }

    public void setLesTijdComboBox() {
        setLesTijdComboBox(0);
    }

    public void setLesTijdComboBox(int lesID) {
        LocalDate date = datePicker.getValue();
        String valueToSelect = null;
        int gebruikerID = gebruiker.getID();
        String query = "SELECT les.begintijd, les.eindtijd, les.lesID, cursus.cursusNaam " +
                "FROM les LEFT " +
                "OUTER JOIN afwezigheid ON les.lesID = afwezigheid.lesID " +
                "INNER JOIN cursus ON les.cursusID = cursus.cursusID " +
                "INNER JOIN leerling ON les.klasID = leerling.klasID " +
                "WHERE les.begintijd >= '" + date + " 00:00:00' " +
                "AND les.begintijd <= '" + date + " 23:59:59' " +
                "AND afwezigheid.leerlingID IS NULL " +
                "AND leerling.gebruikerID = " + gebruikerID;
        ArrayList<Map<String, Object>> lessen = Database.executeStatement(query);
        ObservableList<String> lessenStrings = FXCollections.observableArrayList();
        System.out.println(lessen);
        for (Map<String, Object> les : lessen) {
            String beginTijd = ((LocalDateTime) les.get("begintijd")).getHour() + ":" + ((LocalDateTime) les.get("begintijd")).getMinute();
            String eindTijd = ((LocalDateTime) les.get("eindtijd")).getHour() + ":" + ((LocalDateTime) les.get("eindtijd")).getMinute();
            String lesString =les.get("cursusNaam") + "\t  " + beginTijd + " - " + eindTijd;
            lesIDs.put(lesString, (int) les.get("lesID"));
            lessenStrings.add(lesString);
            if (lesID == (int) les.get("lesID")) {
                valueToSelect = lesString;
            }
        }
        lesTijdComboBox.setItems(lessenStrings);
        lesTijdComboBox.show();                                    //
        lesTijdComboBox.setVisibleRowCount(lessenStrings.size());  // Resize ComboBox dropdown list
        lesTijdComboBox.hide();                                    //
        lesTijdComboBox.setValue(valueToSelect);
    }

    public void selectAnders() {
        redenAfmelding.selectToggle(andersRadioButton);
    }

    public void setDatePicker(LocalDate date) {
        datePicker.setValue(date);
    }

    public void setGebruiker(Gebruiker gebruiker) {
        this.gebruiker = gebruiker;
        datePicker.setValue(LocalDate.now());
        setLesTijdComboBox();
    }
}
