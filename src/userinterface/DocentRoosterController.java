
package userinterface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
import java.util.HashMap;

public class DocentRoosterController {
    mainmenuController parentController;

    @FXML private Label dagLabel;
    @FXML private Label weekLabel;
    @FXML private Label errorLabel;
    @FXML private Label roosternaamLabel;

    @FXML private ListView<String> dagRoosterListView;
    @FXML private ListView<String> weekRoosterListView;

    @FXML private DatePicker overzichtDatePicker;

    private String gebruikersType;
    private School school = School.getSchool();
    private String Lesnaam = "";
    private HashMap<String, Integer> lesIDs;

    public void setParentController(mainmenuController controller) {
        this.parentController = controller;
        roosternaamLabel.setText("" + parentController.getNaamGebruiker());
        toonlessen();
    }

    public void initialize() {
        try {
            overzichtDatePicker.setValue(LocalDate.now());

        } catch (NullPointerException e) {
            errorLabel.setText("Error! couldn't load the data");
        }
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
            lesIDs = new HashMap<>();

            ObservableList<String> dagles = FXCollections.observableArrayList();
            ObservableList<String> weekles = FXCollections.observableArrayList();

            LocalDate dagX = overzichtDatePicker.getValue();
            DateTimeFormatter forX = DateTimeFormatter.ofPattern("w");
            int weekX = Integer.parseInt(dagX.format(forX));

            dagLabel.setText(overzichtDatePicker.getValue().getDayOfWeek().toString().toLowerCase());
            weekLabel.setText("Week : " + weekX);
            for (Rooster x : school.getRooster()){
                if(overzichtDatePicker.getValue().isEqual(x.getlesdagDatum())){
                    String lesinfo="";
                    lesinfo = lesinfo + "Les : " + x.getLes();
                    lesinfo = lesinfo + " | tijd : " + x.getlestijd();
                    dagles.add(lesinfo);
                    lesIDs.put(lesinfo, x.getLesID());
                }
                if(weekX==x.getWeeknummer()){
                    String lesinfo="";
                    lesinfo = lesinfo + "" + x.getlesdagDatum().getDayOfWeek();
                    lesinfo = lesinfo + " | Les : " + x.getLes();
                    lesinfo = lesinfo + " | tijd : " + x.getlestijd();
                    weekles.add(lesinfo);
                    lesIDs.put(lesinfo, x.getLesID());
                }
            }
            dagRoosterListView.setItems(dagles);
            weekRoosterListView.setItems(weekles);
        } catch (NullPointerException e) {
            errorLabel.setText("Error! couldn't load the data");
            e.printStackTrace();
        }
    }

    public void Afwezigheid() {
        try {
            Lesnaam = dagRoosterListView.getSelectionModel().getSelectedItem();

            String fxmlPagina = "AfwezigeStudentenShow.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPagina));
            Parent root = loader.load();

            AfwezigeStudentenShowController controller = loader.getController();
            controller.setLesID(lesIDs.get(dagRoosterListView.getSelectionModel().getSelectedItem()));
            controller.Les(Lesnaam);
            controller.giveDatum(overzichtDatePicker.getValue());
            controller.zetAlleleerlingen();
            controller.zetButtonsVisable(gebruikersType);
            Stage stage = new Stage();
            if(Lesnaam == null || Lesnaam.isEmpty()){
                stage.setTitle("Absentie");
            }else {
                stage.setTitle(Lesnaam);
            }
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            errorLabel.setText("Error! can't access absent melden window ");
            e.printStackTrace();
        }
    }

    public void setUserType(String Usertype){
        gebruikersType = Usertype;
    }
}