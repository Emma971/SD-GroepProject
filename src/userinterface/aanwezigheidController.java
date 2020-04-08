package rooster.userinterface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import rooster.model.Rooster;
import rooster.model.School;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static rooster.model.Database.executeStatement;

public class aanwezigheidController {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy");

    @FXML private Label aanwezigDatumLabel;
    @FXML private Label aanwezigDataLabel;
    @FXML private Label aanwezigCalcLabel;

    @FXML private ListView aaanwezigList;

    @FXML private DatePicker overzichtDatePicker;

    @FXML private TextField aanwezigInputText;

    @FXML private Button aanwezigTonen;

    private School school = School.getSchool();

    public void initialize() {
        try {
            aanwezigDataLabel.setText(mainmenuController.getUsernaam() + ", " + mainmenuController.getUsertype() + ", " + mainmenuController.getUserID());
            overzichtDatePicker.setValue(LocalDate.now());
            aanwezigDatumLabel.setText("" + LocalDate.now().getDayOfWeek() + ", " + LocalDate.now().getDayOfMonth() + "/" + LocalDate.now().getMonth() + "/" + LocalDate.now().getYear());

            aanwezigInputText.setVisible (mainmenuController.getUsertype().equals("Decaan")||mainmenuController.getUsertype().equals("SLB"));
            aanwezigTonen.setVisible (mainmenuController.getUsertype().equals("Decaan")||mainmenuController.getUsertype().equals("SLB"));
            aanwezigCalcLabel.setVisible(mainmenuController.getUsertype().equals("leerling"));

            if (mainmenuController.getUsertype().equals("leerling"))
                toonabsentlessen();
        } catch (NullPointerException e) {
            aanwezigCalcLabel.setText("Error! couldn't load the data");
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

    public void toonabsentlessenDecaan() {
        try {
            ObservableList<String> dagDecaanafwezig = FXCollections.observableArrayList();
            if (aanwezigInputText.getText().contains("klas")){
                String[] textsplit = aanwezigInputText.getText().split("klas");
                String klasnaam = textsplit[1];

            }
            if (aanwezigInputText.getText().contains("leerling")){

            }
            if (aanwezigInputText.getText().contains("cursus")){

            }
            for (Rooster x : school.getRooster()){
                if(overzichtDatePicker.getValue().isEqual(x.getlesdagDatum())){
                    String lesinfo="";
                    lesinfo = lesinfo + "Les : " + x.getLes();
                    lesinfo = lesinfo + " | tijd : " + x.getlestijd();
                    dagDecaanafwezig.add(lesinfo);
                }
            }
            aaanwezigList.setItems(dagDecaanafwezig);
        } catch (NullPointerException e) {
            aanwezigCalcLabel.setText("Error! couldn't load the data");
        }
    }

    public void toonabsentlessen(){
        try {
            ObservableList<String> dagafwezig = FXCollections.observableArrayList();
            Integer absentTeller = 0;

            for (Map<String, Object> getlesID : executeStatement("SELECT LesID FROM afwezigheid WHERE leerlingID =" + mainmenuController.getUserID())) {
                for (Map<String, Object> les : executeStatement("SELECT * FROM les WHERE lesID =" + getlesID.get("lesID") + " ORDER BY begintijd")) {
			        String vakNaam = "";
			        String klasNaam = "";
			        String rawDatum = "";
			        String rawBeginTijd = "";
			        String rawEindTijd = "";

			        for (Map<String, Object> lesnaamq : executeStatement("SELECT cursusNaam FROM cursus WHERE cursusID = " + les.get("cursusID")))
				        vakNaam = lesnaamq.get("cursusNaam").toString();

			        String rawBeginDatum = les.get("begintijd").toString();
			        String[] rawBeginDatumSplit = rawBeginDatum.split(" ");
			        rawDatum = rawDatum + rawBeginDatumSplit[2] + "/" + rawBeginDatumSplit[1] + "/" + rawBeginDatumSplit[5];
			        rawBeginTijd = rawBeginTijd + rawBeginDatumSplit[3];

			        String rawEindDatum = les.get("eindtijd").toString();
			        String[] rawEindDatumSplit = rawEindDatum.split(" ");
			        rawEindTijd = rawEindTijd + rawEindDatumSplit[3];

			        LocalDate lesdatum = LocalDate.parse(rawDatum, dateTimeFormatter);
			        String begintijd = rawBeginTijd.replace(":00", "");
			        String eindtijd = rawEindTijd.replace(":00", "");

                    if(overzichtDatePicker.getValue().isEqual(lesdatum)){
                        String lesinfo="afwezig voor";
                        lesinfo = lesinfo + "Les : " + vakNaam;
                        lesinfo = lesinfo + " | tijd : " + begintijd + " - " + eindtijd;
                    dagafwezig.add(lesinfo);
                    }
                }
                absentTeller++;
            }
            aaanwezigList.setItems(dagafwezig);
            aanwezigCalcLabel.setText("" + overzichtDatePicker.getValue().getDayOfWeek() + ", " + overzichtDatePicker.getValue().getDayOfMonth() + "/" + overzichtDatePicker.getValue().getMonth() + "/" + overzichtDatePicker.getValue().getYear() + ", er is : " + absentTeller + "absent melding");
        } catch (NullPointerException e) {
            aanwezigCalcLabel.setText("Error! couldn't load the data");
        }
    }
}
