package rooster.userinterface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import rooster.model.Rooster;
import rooster.model.School;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class SchoolOverzichtController {
    @FXML private Label roosterTitel;
    @FXML private Label dagLabel;
    @FXML private Label weekLabel;
    @FXML private ListView dagRoosterListView;
    @FXML private ListView weekRoosterListView;
    @FXML private DatePicker overzichtDatePicker;

    private School school = School.getSchool();

    public void initialize() {
        try {
            overzichtDatePicker.setValue(LocalDate.now());
            toonlessen();
        } catch (NullPointerException e) {
            e.printStackTrace();
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
    }
}