
package userinterface;

import Utils.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

    @FXML private TabPane dagWeekTabs;
    @FXML private Tab dagTab;
    @FXML private ListView<String> dagRoosterListView;
    @FXML private Tab weekTab;
    @FXML private ListView<String> weekRoosterListView;

    @FXML private DatePicker overzichtDatePicker;

    private HashMap<String, Integer> lesIDs;
    private Gebruiker gebruiker;

    public void initialize() {
        try {
            overzichtDatePicker.setValue(LocalDate.now());
        } catch (NullPointerException e) {
            errorLabel.setText("Error! couldn't load the data");
        }
    }

    public void clearSelection() {
        if (dagRoosterListView.getSelectionModel() != null) {
            dagRoosterListView.getSelectionModel().clearSelection();
        }
        if (weekRoosterListView.getSelectionModel() != null) {
            weekRoosterListView.getSelectionModel().clearSelection();
        }
    }

    public void toonVanDaag() {
        overzichtDatePicker.setValue(LocalDate.now());
        clearSelection();
    }

    public void toonVorige() {
        LocalDate nieuweDatum;
        if (dagWeekTabs.getSelectionModel().getSelectedItem() == dagTab) {
            nieuweDatum = overzichtDatePicker.getValue().minusDays(1);
        } else {
            nieuweDatum = overzichtDatePicker.getValue().minusDays(7);
        }
        overzichtDatePicker.setValue(nieuweDatum);
        clearSelection();
    }

    public void toonVolgende() {
        LocalDate nieuweDatum;
        if (dagWeekTabs.getSelectionModel().getSelectedItem() == dagTab) {
            nieuweDatum = overzichtDatePicker.getValue().plusDays(1);
        } else if (dagWeekTabs.getSelectionModel().getSelectedItem() == weekTab){
            nieuweDatum = overzichtDatePicker.getValue().plusDays(7);
        } else {
            nieuweDatum = overzichtDatePicker.getValue();  // Unreachable, but circumvents compile error
        }
        overzichtDatePicker.setValue(nieuweDatum);
        clearSelection();
    }

    public void toonlessen() {
        String type = gebruiker.getType();
        if (type.equals("docent")) {
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
                        "WHERE les.docentID = " + gebruiker.getTypeID() + " " +
                        "AND WEEK(les.begintijd) = " + (weekX - 1) + " " +
                        "ORDER BY les.begintijd";
                for (Map<String, Object> data : Database.executeStatement(query)) {
                    System.out.println(data);
                    LocalDateTime begintijd = (LocalDateTime) data.get("begintijd");
                    if (overzichtDatePicker.getValue().isEqual(begintijd.toLocalDate())) {
                        String lesinfo = "";
                        lesinfo = lesinfo + "Les : " + data.get("klasNaam");
                        lesinfo = lesinfo + " | tijd : " + begintijd.toLocalTime();
                        if (!lessenDag.contains(lesinfo))
                            lessenDag.add(lesinfo);
                        lesIDs.put(lesinfo, (int) data.get("lesID"));
                    }
                    String lesinfo = "";
                    lesinfo = lesinfo + begintijd.getDayOfWeek();
                    lesinfo = lesinfo + "  |  Les : " + data.get("klasNaam");
                    lesinfo = lesinfo + " | tijd : " + begintijd.toLocalTime();
                    if (!lessenWeek.contains(lesinfo))
                        lessenWeek.add(lesinfo);
                    lesIDs.put(lesinfo, (int) data.get("lesID"));
                }
                if (lessenDag.isEmpty())
                    lessenDag.add("");
                if (lessenWeek.isEmpty())
                    lessenWeek.add("");
                dagRoosterListView.setItems(lessenDag);
                weekRoosterListView.setItems(lessenWeek);
            } catch (NullPointerException e) {
                errorLabel.setText("Error! couldn't load the data");
                e.printStackTrace();
            }
        } else if (type.equals("slb")){
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
                        "INNER JOIN leerling ON  les.klasID = leerling.klasID " +
                        "WHERE leerling.SLBID = " + gebruiker.getTypeID() + " " +
                        "AND WEEK(les.begintijd) = " + (weekX - 1) + " " +
                        "ORDER BY les.begintijd";
                for (Map<String, Object> data : Database.executeStatement(query)) {
                    System.out.println(data);
                    LocalDateTime begintijd = (LocalDateTime) data.get("begintijd");
                    if(overzichtDatePicker.getValue().isEqual(begintijd.toLocalDate())){
                        String lesinfo="";
                        lesinfo = lesinfo + "Les : " + data.get("klasNaam");
                        lesinfo = lesinfo + " | tijd : " + begintijd.toLocalTime();
                        if (!lessenDag.contains(lesinfo))
                            lessenDag.add(lesinfo);
                        lesIDs.put(lesinfo, (int) data.get("lesID"));
                    }
                    String lesinfo = "";
                    lesinfo = lesinfo + begintijd.getDayOfWeek();
                    lesinfo = lesinfo + "  |  Les : " + data.get("klasNaam");
                    lesinfo = lesinfo + " | tijd : " + begintijd.toLocalTime();
                    if (!lessenWeek.contains(lesinfo))
                        lessenWeek.add(lesinfo);
                    lesIDs.put(lesinfo, (int) data.get("lesID"));
                }
                if (lessenDag.isEmpty())
                    lessenDag.add("");
                if (lessenWeek.isEmpty())
                    lessenWeek.add("");
                dagRoosterListView.setItems(lessenDag);
                weekRoosterListView.setItems(lessenWeek);
            } catch (NullPointerException e) {
                errorLabel.setText("Error! couldn't load the data");
                e.printStackTrace();
            }
        }
    }

    public void Afwezigheid() {
        try {
            String lesnaam = dagRoosterListView.getSelectionModel().getSelectedItem();

            String fxmlPagina = "AfwezigeStudentenShow.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPagina));
            Parent root = loader.load();

            AfwezigeStudentenShowController controller = loader.getController();
            controller.setLesID(lesIDs.get(dagRoosterListView.getSelectionModel().getSelectedItem()));
            controller.Les(lesnaam);
            controller.giveDatum(overzichtDatePicker.getValue());
            controller.zetAlleleerlingen();
            controller.zetButtonsVisable(gebruiker.getType());
            controller.setGebruiker(gebruiker);
            Stage stage = new Stage();
            if(lesnaam == null || lesnaam.isEmpty()){
                stage.setTitle("Absentie");
            }else {
                stage.setTitle(lesnaam);
            }
            stage.setScene(new Scene(root));
            stage.show();
            root.requestFocus();
        } catch (IOException e) {
            errorLabel.setText("Error! can't access absent melden window ");
            e.printStackTrace();
        }
    }

    public void setGebruiker(Gebruiker gebruiker) {
        this.gebruiker = gebruiker;
        roosternaamLabel.setText("" + gebruiker.getNaam());
        toonlessen();
    }
}