package userinterface;

import Utils.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

public class aanwezigheidController {

    @FXML private Label aanwezigDatumLabel;
    @FXML private Label aanwezigDataLabel;
    @FXML private Label aanwezigCalcLabel;

    @FXML private ListView<String> aanwezigList;

    @FXML private DatePicker overzichtDatePicker;

    @FXML private Button aanwezigTonen;

    @FXML private ComboBox<String> aanwezigheidComboBox;
    @FXML private ComboBox<String> aanwezigheidComboBoxantwoord;

    private Gebruiker gebruiker;

    public void setGebruiker(Gebruiker gebruiker) {
        this.gebruiker = gebruiker;
        overzichtDatePicker.setValue(LocalDate.now());
        try {
            ObservableList<String> list = FXCollections.observableArrayList();
            String comboBoxklas = "klas";
            String comboBoxleerling = "leerling";
            list.add(comboBoxklas);
            list.add(comboBoxleerling);
            aanwezigheidComboBox.setItems(list);

            aanwezigDataLabel.setText(gebruiker.getNaam() + ", " + gebruiker.getType() + ", " + gebruiker.getID());
            overzichtDatePicker.setValue(LocalDate.now());
            aanwezigDatumLabel.setText("" + LocalDate.now().getDayOfWeek().toString().toLowerCase() + ", " + LocalDate.now().getDayOfMonth() + "/" + LocalDate.now().getMonth().toString().toLowerCase() + "/" + LocalDate.now().getYear());

            aanwezigTonen.setVisible(gebruiker.getType().equals("decaan"));
            aanwezigheidComboBox.setVisible(gebruiker.getType().equals("decaan"));
            aanwezigCalcLabel.setVisible(gebruiker.getType().equals("leerling"));

            if (gebruiker.getType().equals("leerling")) {
                toonabsentlessen();
            } else {
                aanwezigList.setItems(FXCollections.observableArrayList(""));
            }
        } catch (NullPointerException e) {
            Popup.alert("Error! couldn't load the data");
            e.printStackTrace();
        }
    }

    public void toonVanDaag() {
        overzichtDatePicker.setValue(LocalDate.now());
        String type = gebruiker.getType();
        if (type.equals("decaan")) {
            toonabsentlessenDecaan();
        } else if (type.equals("slb")) {
            toonabsentlessenSLB();
        } else {
            toonabsentlessen();
        }
    }

    public void toonVorigeDag() {
        LocalDate dagEerder = overzichtDatePicker.getValue().minusDays(1);
        overzichtDatePicker.setValue(dagEerder);
        String type = gebruiker.getType();
        if (type.equals("decaan")) {
            toonabsentlessenDecaan();
        } else if (type.equals("slb")) {
            toonabsentlessenSLB();
        } else {
            toonabsentlessen();
        }
    }

    public void toonVolgendeDag() {
        LocalDate dagLater = overzichtDatePicker.getValue().plusDays(1);
        overzichtDatePicker.setValue(dagLater);
        String type = gebruiker.getType();
        if (type.equals("decaan")) {
            toonabsentlessenDecaan();
        } else if (type.equals("slb")) {
            toonabsentlessenSLB();
        } else {
            toonabsentlessen();
        }
    }

    public void toonabsentlessenDecaan() {
        try {
            ObservableList<String> comboboxData = FXCollections.observableArrayList();
            ObservableList<String> dagDecaanafwezig = FXCollections.observableArrayList();
            LocalDate datum = overzichtDatePicker.getValue();
            if (aanwezigheidComboBox.getValue().equals("klas")) {
                aanwezigheidComboBoxantwoord.setVisible(aanwezigheidComboBox.getValue().equals("klas"));
                String query = "SELECT klas.klasNaam " +
                        "FROM klas " +
                        "INNER JOIN medewerkertoegangklas ON klas.klasID = medewerkertoegangklas.klasID " +
                        "WHERE medewerkertoegangklas.medewerkerID = " + gebruiker.getTypeID() + " " +
                        "ORDER BY klas.klasNaam";
                ArrayList<Map<String, Object>> klasnaamlist = Database.executeStatement(query);
                for (Map<String, Object> klasnaam : klasnaamlist) {
                    String klas = (String) klasnaam.get("klasNaam");
                    comboboxData.add(klas);
                    aanwezigheidComboBoxantwoord.setItems(comboboxData);
                }
                query = "SELECT gebruiker.naam, afwezigheid.reden, cursus.cursusNaam, les.begintijd, les.eindtijd " +
                        "FROM afwezigheid " +
                        "INNER JOIN leerling ON afwezigheid.leerlingID = leerling.leerlingID " +
                        "INNER JOIN gebruiker ON leerling.gebruikerID = gebruiker.gebruikerID " +
                        "INNER JOIN les ON afwezigheid.lesID = les.lesID " +
                        "INNER JOIN cursus ON les.cursusID = cursus.cursusID " +
                        "INNER JOIN klas ON cursus.klasID = klas.klasID " +
                        "INNER JOIN medewerkertoegangklas ON les.klasID = medewerkertoegangklas.klasID " +
                        "WHERE medewerkertoegangklas.medewerkerID = " + gebruiker.getTypeID() + " " +
                        "AND klas.klasNaam = '" + aanwezigheidComboBoxantwoord.getValue() + "' " +
                        "AND les.begintijd >= '" + datum + " 00:00:00' " +
                        "AND les.begintijd <= '" + datum + " 23:59:59' " +
                        "ORDER BY les.begintijd";
                ArrayList<Map<String, Object>> data = Database.executeStatement(query);
                System.out.println(data);
                for (Map<String, Object> afmelding : data) {
                    String naamStudent = (String) afmelding.get("naam");
                    String naamCursus = (String) afmelding.get("cursusNaam");
                    LocalDateTime begintijd = (LocalDateTime) afmelding.get("begintijd");
                    LocalDateTime eindtijd = (LocalDateTime) afmelding.get("eindtijd");
                    String reden = (String) afmelding.get("reden");
                    if (datum.isEqual(begintijd.toLocalDate())) {
                        String lesinfo = naamStudent + " was afwezig voor" +
                                " Les : " + naamCursus +
                                " | tijd : " + begintijd.getHour() + ":" + begintijd.getMinute() + " - " + eindtijd.getHour() + ":" + eindtijd.getMinute() +
                                " | reden : " + reden;
                        dagDecaanafwezig.add(lesinfo);
                    }
                }
            }
            if (aanwezigheidComboBox.getValue().equals("leerling")) {
                aanwezigheidComboBoxantwoord.setVisible(aanwezigheidComboBox.getValue().equals("leerling"));
                String query = "SELECT gebruiker.naam " +
                        "FROM gebruiker " +
                        "INNER JOIN leerling ON gebruiker.gebruikerID = leerling.gebruikerID " +
                        "INNER JOIN medewerkertoegangklas ON leerling.klasID = medewerkertoegangklas.klasID " +
                        "WHERE medewerkertoegangklas.medewerkerID = " + gebruiker.getTypeID() + " " +
                        "ORDER BY gebruiker.naam";
                ArrayList<Map<String, Object>> leerlingnaamlist = Database.executeStatement(query);
                for (Map<String, Object> leerlingnaam : leerlingnaamlist) {
                    String naam = (String) leerlingnaam.get("naam");
                    comboboxData.add(naam);
                    aanwezigheidComboBoxantwoord.setItems(comboboxData);
                }
                query = "SELECT gebruiker.naam, afwezigheid.reden, cursus.cursusNaam, les.begintijd, les.eindtijd " +
                        "FROM afwezigheid " +
                        "INNER JOIN les ON afwezigheid.lesID = les.lesID " +
                        "INNER JOIN cursus ON les.cursusID = cursus.cursusID " +
                        "INNER JOIN leerling on afwezigheid.leerlingID = leerling.leerlingID " +
                        "INNER JOIN gebruiker on leerling.gebruikerID = gebruiker.gebruikerID " +
                        "INNER JOIN klas on leerling.klasID = klas.klasID " +
                        "INNER JOIN medewerkertoegangklas ON les.klasID = medewerkertoegangklas.klasID " +
                        "WHERE medewerkertoegangklas.medewerkerID = " + gebruiker.getTypeID() + " " +
                        "AND gebruiker.naam = '" + aanwezigheidComboBoxantwoord.getValue() + "'" +
                        "AND les.begintijd >= '" + datum + " 00:00:00' " +
                        "AND les.begintijd <= '" + datum + " 23:59:59' " +
                        "ORDER BY les.begintijd";
                ArrayList<Map<String, Object>> data = Database.executeStatement(query);
                System.out.println(data);
                for (Map<String, Object> afmelding : data) {
                    String naamStudent = (String) afmelding.get("naam");
                    String naamCursus = (String) afmelding.get("cursusNaam");
                    LocalDateTime begintijd = (LocalDateTime) afmelding.get("begintijd");
                    LocalDateTime eindtijd = (LocalDateTime) afmelding.get("eindtijd");
                    String reden = (String) afmelding.get("reden");
                    if (datum.isEqual(begintijd.toLocalDate())) {
                        String lesinfo = naamStudent + " was afwezig voor" +
                                " Les : " + naamCursus +
                                " | tijd : " + begintijd.getHour() + ":" + begintijd.getMinute() + " - " + eindtijd.getHour() + ":" + eindtijd.getMinute() +
                                " | reden : " + reden;
                        dagDecaanafwezig.add(lesinfo);
                    }
                }
            }

            if (dagDecaanafwezig.isEmpty()) {
                dagDecaanafwezig.add("");
            }
            aanwezigList.setItems(dagDecaanafwezig);
        } catch (NullPointerException e) {
            Popup.alert("Error! couldn't load the data");
            e.printStackTrace();
        }
    }

    public void toonabsentlessenSLB() {
        try {
            int slbID = gebruiker.getTypeID();

            ObservableList<String> dagafwezig = FXCollections.observableArrayList();

            String query = "SELECT gebruiker.naam, klas.klasNaam, cursus.cursusNaam, les.begintijd, afwezigheid.reden " +
                    "FROM afwezigheid " +
                    "INNER JOIN les ON afwezigheid.lesID = les.lesID " +
                    "INNER JOIN cursus ON les.cursusID = cursus.cursusID " +
                    "INNER JOIN klas ON les.klasID = klas.klasID " +
                    "INNER JOIN leerling ON afwezigheid.leerlingID = leerling.leerlingID " +
                    "INNER JOIN gebruiker ON leerling.gebruikerID = gebruiker.gebruikerID " +
                    "WHERE SLBID =" + slbID;
            for (Map<String, Object> data : Database.executeStatement(query)) {
                String studentnaam = (String) data.get("naam");
                String klasnaam = (String) data.get("klasNaam");
                String vaknaam = (String) data.get("cursusNaam");
                String reden = (String) data.get("reden");

                LocalDateTime datumTijdBegin = (LocalDateTime) data.get("begintijd");
                LocalDate lesDatum = datumTijdBegin.toLocalDate();
                System.out.println(data);
                if (overzichtDatePicker.getValue().isEqual(lesDatum)) {
                    String lesinfo = studentnaam + " van "+ klasnaam + " was afwezig voor: "+ vaknaam +
                            " | Reden: " + reden;
                    if (!dagafwezig.contains(lesinfo))
                        dagafwezig.add(lesinfo);
                }
            }
            if (dagafwezig.isEmpty())
                dagafwezig.add("");
            aanwezigList.setItems(dagafwezig);
            aanwezigCalcLabel.setText(overzichtDatePicker.getValue().getDayOfWeek().toString().toLowerCase() +
                    ", " + overzichtDatePicker.getValue().getDayOfMonth() +
                    "/" + overzichtDatePicker.getValue().getMonth().toString().toLowerCase() +
                    "/" + overzichtDatePicker.getValue().getYear());
        } catch (NullPointerException e) {
            Popup.alert("Error! couldn't load the data");
            e.printStackTrace();
        }
    }

    public void toonabsentlessen() {
        try {
            int leerID = gebruiker.getTypeID();
            ObservableList<String> dagafwezig = FXCollections.observableArrayList();

            String query = "SELECT cursus.cursusNaam, les.begintijd, les.eindtijd, afwezigheid.reden " +
                    "FROM afwezigheid " +
                    "INNER JOIN les ON afwezigheid.lesID = les.lesID " +
                    "INNER JOIN cursus ON les.cursusID = cursus.cursusID " +
                    "WHERE leerlingID =" + leerID;
            for (Map<String, Object> data : Database.executeStatement(query)) {
                    String vaknaam = (String) data.get("cursusNaam");
                    String reden = (String) data.get("reden");
                    LocalDateTime datumTijdBegin = (LocalDateTime) data.get("begintijd");
                    LocalDateTime datumTijdEind = (LocalDateTime) data.get("eindtijd");
                    LocalDate lesDatum = datumTijdBegin.toLocalDate();

                    if (overzichtDatePicker.getValue().isEqual(lesDatum)) {
                        String lesinfo = "afwezig voor: " + vaknaam +
                                " <" + datumTijdBegin.getHour() + "." + datumTijdBegin.getMinute() +
                                " - " + datumTijdEind.getHour() + "." + datumTijdEind.getMinute() +
                                ">  reden: " + reden;
                        dagafwezig.add(lesinfo);
                    }
                }
            if (dagafwezig.isEmpty())
                dagafwezig.add("");
            aanwezigList.setItems(dagafwezig);
            aanwezigCalcLabel.setText("" + overzichtDatePicker.getValue().getDayOfWeek().toString().toLowerCase() + ", " + overzichtDatePicker.getValue().getDayOfMonth() + "/" + overzichtDatePicker.getValue().getMonth().toString().toLowerCase() + "/" + overzichtDatePicker.getValue().getYear());
        } catch (NullPointerException e) {
            Popup.alert("Error! couldn't load the data");
            e.printStackTrace();
        }
    }
}
