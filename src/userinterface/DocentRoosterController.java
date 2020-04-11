
package userinterface;

import Utils.*;
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

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class DocentRoosterController {

    @FXML private Label dagLabel;
    @FXML private Label weekLabel;
    @FXML private Label errorLabel;
    @FXML private Label roosternaamLabel;

    @FXML private ListView<String> dagRoosterListView;
    @FXML private ListView<String> weekRoosterListView;

    @FXML private DatePicker overzichtDatePicker;

    private String gebruikersType;
    private String Lesnaam = "";
    private HashMap<String, Integer> lesIDs;
    private Gebruiker gebruiker;

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

            ObservableList<String> lessenDag = FXCollections.observableArrayList();
            ObservableList<String> lessenWeek = FXCollections.observableArrayList();

            LocalDate dagX = overzichtDatePicker.getValue();
            DateTimeFormatter forX = DateTimeFormatter.ofPattern("w");
            int weekX = Integer.parseInt(dagX.format(forX));

            dagLabel.setText(overzichtDatePicker.getValue().getDayOfWeek().toString().toLowerCase());
            weekLabel.setText("Week : " + weekX);
            String query = "SELECT les.begintijd, klas.klasNaam, les.lesID " +
                    "FROM les " +
                    "INNER JOIN klas ON les.klasID = klas.klasID " +
                    "INNER JOIN medewerker ON les.docentID = medewerker.medewerkerID " +
                    "WHERE medewerker.gebruikerID = " + gebruiker.getID() + " " +
                    "AND WEEK(les.begintijd) = " + (weekX - 1);
            for (Map<String, Object> data : Database.executeStatement(query)) {
                System.out.println(data);
                LocalDateTime begintijd = (LocalDateTime) data.get("begintijd");
                if(overzichtDatePicker.getValue().isEqual(begintijd.toLocalDate())){
                    String lesinfo="";
                    lesinfo = lesinfo + "Les : " + data.get("klasNaam");
                    lesinfo = lesinfo + " | tijd : " + begintijd.toLocalTime();
                    lessenDag.add(lesinfo);
                    lesIDs.put(lesinfo, (int) data.get("lesID"));
                }
                String lesinfo = "";
                lesinfo = lesinfo + begintijd.getDayOfWeek();
                lesinfo = lesinfo + "  |  Les : " + data.get("klasNaam");
                lesinfo = lesinfo + " | tijd : " + begintijd.toLocalTime();
                lessenWeek.add(lesinfo);
                lesIDs.put(lesinfo, (int) data.get("lesID"));
            }
            dagRoosterListView.setItems(lessenDag);
            weekRoosterListView.setItems(lessenWeek);
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
            controller.setGebruiker(gebruiker);
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

    public void setGebruiker(Gebruiker gebruiker) {
        this.gebruiker = gebruiker;
        roosternaamLabel.setText("" + gebruiker.getNaam());
        toonlessen();
    }
}