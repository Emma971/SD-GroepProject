package userinterface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static Utils.Database.executeStatement;

public class aanwezigheidController {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy");

    @FXML private Label aanwezigDatumLabel;
    @FXML private Label aanwezigDataLabel;
    @FXML private Label aanwezigCalcLabel;
    @FXML private Label aanwezigStudentLabel;

    @FXML private ListView<String> aaanwezigList;

    @FXML private DatePicker overzichtDatePicker;

    @FXML private TextField aanwezigInputText;

    @FXML private Button aanwezigTonen;

    @FXML private ComboBox<String> aanwezigheidComboBox;
    private Object ObservableList;

    public void initialize() {
        try {
            ObservableList<String> list = FXCollections.observableArrayList();
            String comboBoxklas = "klas";
            String comboBoxleerling = "leerling";
            String comboBoxcursus = "cursus";
            list.add(comboBoxklas);
            list.add(comboBoxleerling);
            list.add(comboBoxcursus);
            aanwezigheidComboBox.setItems(list);

            aanwezigDataLabel.setText(mainmenuController.getUsername() + ", " + mainmenuController.getUsertype() + ", " + mainmenuController.getUserID());
            overzichtDatePicker.setValue(LocalDate.now());
            aanwezigDatumLabel.setText("" + LocalDate.now().getDayOfWeek() + ", " + LocalDate.now().getDayOfMonth() + "/" + LocalDate.now().getMonth() + "/" + LocalDate.now().getYear());

            aanwezigInputText.setVisible (mainmenuController.getUsertype().equals("Decaan")||mainmenuController.getUsertype().equals("SLB"));
            aanwezigTonen.setVisible (mainmenuController.getUsertype().equals("Decaan")||mainmenuController.getUsertype().equals("SLB"));
            aanwezigheidComboBox.setVisible(mainmenuController.getUsertype().equals("Decaan")||mainmenuController.getUsertype().equals("SLB"));
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
        toonabsentlessenDecaan();
    }

    public void toonVolgendeDag(ActionEvent actionEvent) {
        LocalDate dagLater = overzichtDatePicker.getValue().plusDays(1);
        overzichtDatePicker.setValue(dagLater);
        toonabsentlessenDecaan();
    }

    public void toonabsentlessenDecaan() {
        try {
            ObservableList<String> dagDecaanafwezig = FXCollections.observableArrayList();
            String[] textsplit = aanwezigInputText.getText().split(" ");
            if (aanwezigheidComboBox.getValue().equals("klas")){
                String klasnaam = aanwezigInputText.getText();
                for (Map<String, Object> klasIDD : executeStatement("SELECT klasID FROM klas WHERE klasNaam = '" + klasnaam + "'")) {
                    String studentenaam = "";
                    for (Map<String, Object> leerlingIDD : executeStatement("SELECT leerlingID,gebruikerID FROM leerling WHERE klasID = " + klasIDD.get("klasID"))) {
                        String studentID = leerlingIDD.get("gebruikerID").toString();
                        for (Map<String, Object> leerlingnaam : executeStatement("SELECT naam FROM gebruiker WHERE gebruikerID = " + studentID)){
                            studentenaam = leerlingnaam.get("naam").toString();}
                            for (Map<String, Object> getlesID : executeStatement("SELECT LesID FROM afwezigheid WHERE leerlingID =" + leerlingIDD.get("leerlingID"))) {
                                for (Map<String, Object> les : executeStatement("SELECT * FROM les WHERE lesID =" + getlesID.get("lesID") + " ORDER BY begintijd")) {
                                String vakNaam = "";

                                    LocalDateTime begint = (LocalDateTime) les.get("begintijd");

                                    LocalDateTime eindt = (LocalDateTime) les.get("eindtijd");

                                    LocalDate lesdatum = begint.toLocalDate();

                                    LocalDateTime begintijd = begint;
                                    LocalDateTime eindtijd = eindt;


                                for (Map<String, Object> lesnaamq : executeStatement("SELECT cursusNaam FROM cursus WHERE cursusID = " + les.get("cursusID")))
                                    vakNaam = lesnaamq.get("cursusNaam").toString();

                                if (overzichtDatePicker.getValue().isEqual(lesdatum)) {
                                    String lesinfo = "";
                                    lesinfo = studentenaam + " was afwezig voor";
                                    lesinfo = lesinfo + " Les : " + vakNaam;
                                    lesinfo = lesinfo + " | tijd : "+ begintijd.getHour()+":"+begintijd.getMinute() + " - " + eindtijd.getHour()+":"+eindtijd.getMinute();
                                    dagDecaanafwezig.add(lesinfo);
                                }

                            }
                            }
                        }
                }
            }
            if (aanwezigheidComboBox.getValue().equals("leerling")) {
                String vaknaam = "";
                String leerlingID = aanwezigInputText.getText();
                for (Map<String, Object> afwezig : executeStatement("SELECT gebruiker.naam, afwezigheid.reden, cursus.cursusNaam, les.begintijd, les.eindtijd FROM afwezigheid INNER JOIN les ON afwezigheid.lesID = les.lesID INNER JOIN cursus ON les.cursusID = cursus.cursusID INNER JOIN leerling on afwezigheid.leerlingID = leerling.leerlingID INNER JOIN gebruiker on leerling.gebruikerID = gebruiker.gebruikerID WHERE afwezigheid.leerlingID = " + leerlingID + ";")) {
                    String reden = (String) afwezig.get("reden");
                    String cursusNaam = (String) afwezig.get("cursusNaam");
                    LocalDateTime begintijd = (LocalDateTime) afwezig.get("begintijd");
                    LocalDateTime eindtijd = (LocalDateTime) afwezig.get("eindtijd");
                    String studentnaam = afwezig.get("naam").toString();
                    LocalDate lesdatum = begintijd.toLocalDate();

                        if (overzichtDatePicker.getValue().isEqual(lesdatum)) {
                            String lesinfo = "";
                            lesinfo = studentnaam + " was afwezig voor ";
                            lesinfo = lesinfo + "Les : " + vaknaam;
                            lesinfo = lesinfo + " | tijd : "+ begintijd.getHour()+":"+begintijd.getMinute() + " - " + eindtijd.getHour()+":"+eindtijd.getMinute();
                            lesinfo = lesinfo + " | reden : "+ reden;
                            dagDecaanafwezig.add(lesinfo);
                    }
            }
            }
            if (aanwezigheidComboBox.getValue().equals("cursus")) {
                String vaknaam = "";
                String cursusID = aanwezigInputText.getText();
                for (Map<String, Object> afwezig : executeStatement("SELECT gebruiker.naam, afwezigheid.reden, cursus.cursusNaam, les.begintijd, les.eindtijd FROM afwezigheid INNER JOIN les ON afwezigheid.lesID = les.lesID INNER JOIN cursus ON les.cursusID = cursus.cursusID INNER JOIN leerling on afwezigheid.leerlingID = leerling.leerlingID INNER JOIN gebruiker on leerling.gebruikerID = gebruiker.gebruikerID WHERE cursus.cursusID = " + cursusID + ";")) {
                    String reden = (String) afwezig.get("reden");
                    String cursusNaam = (String) afwezig.get("cursusNaam");
                    LocalDateTime begintijd = (LocalDateTime) afwezig.get("begintijd");
                    LocalDateTime eindtijd = (LocalDateTime) afwezig.get("eindtijd");
                    String studentnaam = afwezig.get("naam").toString();
                    LocalDate lesdatum = begintijd.toLocalDate();

                    if (overzichtDatePicker.getValue().isEqual(lesdatum)) {
                        String lesinfo = "";
                        lesinfo = studentnaam + " was afwezig voor ";
                        lesinfo = lesinfo + "Les : " + vaknaam;
                        lesinfo = lesinfo + " | tijd : "+ begintijd.getHour()+":"+begintijd.getMinute() + " - " + eindtijd.getHour()+":"+eindtijd.getMinute();
                        lesinfo = lesinfo + " | reden : "+ reden;
                        dagDecaanafwezig.add(lesinfo);
                    }
            }
            }

            if (dagDecaanafwezig.isEmpty()){
                dagDecaanafwezig.add("");
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
