package userinterface;

import Utils.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class SchoolOverzichtController {
    mainmenuController parentController;

    @FXML private Label dagLabel;
    @FXML private Label weekLabel;
    @FXML private Label errorLabel;
    @FXML private Label roosternaamLabel;

    @FXML private ListView<String> dagRoosterListView;
    @FXML private ListView<String> weekRoosterListView;

    @FXML private DatePicker overzichtDatePicker;

    private HashMap<String, Integer> lesIDs = new HashMap<>();

    public void setParentController(mainmenuController controller) {
        try {
            this.parentController = controller;
            roosternaamLabel.setText("" + parentController.getNaamGebruiker() + " Klas : " + parentController.getUserklasnaam());
            toonlessen();
        } catch (NullPointerException e) {
            errorLabel.setText("Error! couldn't load the data");
        }
    }

    public void initialize() {
            overzichtDatePicker.setValue(LocalDate.now());

    }

    public void toonVanDaag() {
        overzichtDatePicker.setValue(LocalDate.now());
    }

    public void toonVorigeDag() {
        LocalDate dagEerder = overzichtDatePicker.getValue().minusDays(1);
        overzichtDatePicker.setValue(dagEerder);
    }

    public void toonVolgendeDag() {
        LocalDate dagLater = overzichtDatePicker.getValue().plusDays(1);
        overzichtDatePicker.setValue(dagLater);
    }

    public void toonlessen() {
        try {
        ObservableList<String> lessenDag = FXCollections.observableArrayList();
        ObservableList<String> lessenWeek = FXCollections.observableArrayList();

        LocalDate systeemDag = overzichtDatePicker.getValue();
        DateTimeFormatter weekformat = DateTimeFormatter.ofPattern("w");
        int weekNummer = Integer.parseInt(systeemDag.format(weekformat));

        dagLabel.setText("" + overzichtDatePicker.getValue().getDayOfWeek());
        weekLabel.setText("Week : " + weekNummer);
        for (Map<String, Object> les : Database.executeStatement("SELECT les.begintijd, cursus.cursusNaam, les.lesID FROM les INNER JOIN cursus ON les.cursusID = cursus.cursusID INNER JOIN leerling ON les.klasID = leerling.klasID WHERE leerling.gebruikerID = " + parentController.getGebruikerID() + " AND WEEK(les.begintijd) = " + (weekNummer - 1))) {
            System.out.println(les);
            LocalDateTime begintijd = (LocalDateTime) les.get("begintijd");
            if(overzichtDatePicker.getValue().isEqual(begintijd.toLocalDate())){
                String lesinfo="";
                lesinfo = lesinfo + "Les : " + les.get("cursusNaam");
                lesinfo = lesinfo + " | tijd : " + begintijd.toLocalTime();
                lessenDag.add(lesinfo);
                lesIDs.put(lesinfo, (int) les.get("lesID"));
            }
            String lesinfo = "";
            lesinfo = lesinfo + begintijd.getDayOfWeek();
            lesinfo = lesinfo + "  |  Les : " + les.get("cursusNaam");
            lesinfo = lesinfo + " | tijd : " + begintijd.toLocalTime();
            lessenWeek.add(lesinfo);
            lesIDs.put(lesinfo, (int) les.get("lesID"));
        }
//        for (Rooster x : school.getRooster()){
//            if(overzichtDatePicker.getValue().isEqual(x.getlesdagDatum())){
//                String lesinfo="";
//                lesinfo = lesinfo + "Les : " + x.getLes();
//                lesinfo = lesinfo + " | tijd : " + x.getlestijd();
//                dagles.add(lesinfo);
//            }
//
//            if(weekNummer==x.getRoosterWeeknummer()){
//                String lesinfo="";
//                lesinfo = lesinfo + "" + x.getlesdagDatum().getDayOfWeek();
//                lesinfo = lesinfo + " | Les : " + x.getLes();
//                lesinfo = lesinfo + " | tijd : " + x.getlestijd();
//                weekles.add(lesinfo);
//            }
//        }
        if (lessenDag.isEmpty()){lessenDag.add("");}
        if (lessenWeek.isEmpty()){lessenWeek.add("");}
        dagRoosterListView.setItems(lessenDag);
        weekRoosterListView.setItems(lessenWeek);
        } catch (NullPointerException e) {
            errorLabel.setText("Error! couldn't load the data");
        }
    }

    public void absentmelden() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("AfmeldenLes.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            AfmeldenLesController controller = fxmlLoader.getController();
            controller.setParentController(this);
            String dagRoosterSelectedLes = dagRoosterListView.getSelectionModel().getSelectedItem();
            if (dagRoosterSelectedLes != null) {
                controller.setDatePicker(overzichtDatePicker.getValue());
//                controller.setLesTijdComboBox();
            }
            Stage stage = new Stage();
            stage.setTitle("Absent Melden");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            errorLabel.setText("Error! can't access absent melden window ");
        }
    }
}