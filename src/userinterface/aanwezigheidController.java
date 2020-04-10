package userinterface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import static Utils.Database.executeStatement;

public class aanwezigheidController {
    mainmenuController parentController;

    @FXML private Label aanwezigDatumLabel;
    @FXML private Label aanwezigDataLabel;
    @FXML private Label aanwezigCalcLabel;
    @FXML private Label aanwezigStudentLabel;

    @FXML private ListView<String> aaanwezigList;

    @FXML private DatePicker overzichtDatePicker;

    @FXML private TextField aanwezigInputText;

    @FXML private Button aanwezigTonen;

    @FXML private ComboBox<String> aanwezigheidComboBox;

    public void setParentController(mainmenuController controller) {
        this.parentController = controller;
        try {
            ObservableList<String> list = FXCollections.observableArrayList();
            String comboBoxklas = "klas";
            String comboBoxleerling = "leerling";
            String comboBoxcursus = "cursus";
            list.add(comboBoxklas);
            list.add(comboBoxleerling);
            list.add(comboBoxcursus);
            aanwezigheidComboBox.setItems(list);

            aanwezigDataLabel.setText(parentController.getNaamGebruiker() + ", " + parentController.getUsertype() + ", " + parentController.getGebruikerID());
            overzichtDatePicker.setValue(LocalDate.now());
            aanwezigDatumLabel.setText("" + LocalDate.now().getDayOfWeek().toString().toLowerCase() + ", " + LocalDate.now().getDayOfMonth() + "/" + LocalDate.now().getMonth().toString().toLowerCase() + "/" + LocalDate.now().getYear());

            aanwezigInputText.setVisible (parentController.getUsertype().equals("decaan"));
            aanwezigTonen.setVisible (parentController.getUsertype().equals("decaan"));
            aanwezigheidComboBox.setVisible(parentController.getUsertype().equals("decaan"));
            aanwezigCalcLabel.setVisible(parentController.getUsertype().equals("leerling"));

            if (parentController.getUsertype().equals("leerling"))
                toonabsentlessen();
        } catch (NullPointerException e) {
            aanwezigCalcLabel.setText("Error! couldn't load the data");
        }
    }

    public void initialize() {

    }

    public void toonVanDaag() {
        overzichtDatePicker.setValue(LocalDate.now());
        String type = parentController.getUsertype();
        if (type.equals("decaan")) {
            toonabsentlessenDecaan();
        } else if (type.equals("slb")){
            toonabsentlessenSLB();
        }else {
            toonabsentlessen();
        }
    }


    public void toonVorigeDag() {
        LocalDate dagEerder = overzichtDatePicker.getValue().minusDays(1);
        overzichtDatePicker.setValue(dagEerder);
        String type = parentController.getUsertype();
        if (type.equals("decaan")) {
            toonabsentlessenDecaan();
        } else if (type.equals("slb")){
                toonabsentlessenSLB();
            }else {
                toonabsentlessen();
            }
    }

    public void toonVolgendeDag() {
        LocalDate dagLater = overzichtDatePicker.getValue().plusDays(1);
        overzichtDatePicker.setValue(dagLater);
        String type = parentController.getUsertype();
        if (type.equals("decaan")) {
            toonabsentlessenDecaan();
        } else if (type.equals("slb")){
            toonabsentlessenSLB();
        }else {
            toonabsentlessen();
        }
    }

    public void toonabsentlessenDecaan() {
        try {
            ObservableList<String> dagDecaanafwezig = FXCollections.observableArrayList();
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

                                    LocalDateTime begintijd = (LocalDateTime) les.get("begintijd");

                                    LocalDateTime eindtijd = (LocalDateTime) les.get("eindtijd");

                                    LocalDate lesdatum = begintijd.toLocalDate();


                                    for (Map<String, Object> lesnaamq : executeStatement("SELECT cursusNaam FROM cursus WHERE cursusID = " + les.get("cursusID")))
                                    vakNaam = lesnaamq.get("cursusNaam").toString();

                                if (overzichtDatePicker.getValue().isEqual(lesdatum)) {
                                    String lesinfo = "";
                                    lesinfo = studentenaam + " was afwezig voor";
                                    lesinfo = lesinfo + " Les : " + vakNaam;
                                    lesinfo = lesinfo + " | tijd : "+ begintijd.getHour()+":"+ begintijd.getMinute() + " - " + eindtijd.getHour()+":"+ eindtijd.getMinute();
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
                for (Map<String, Object> afwezig : executeStatement("SELECT gebruiker.naam, afwezigheid.reden, cursus.cursusNaam, les.begintijd, les.eindtijd FROM afwezigheid INNER JOIN les ON afwezigheid.lesID = les.lesID INNER JOIN cursus ON les.cursusID = cursus.cursusID INNER JOIN leerling on afwezigheid.leerlingID = leerling.leerlingID INNER JOIN gebruiker on leerling.gebruikerID = gebruiker.gebruikerID WHERE cursus.cursusID = '" + cursusID + "';")) {
                    String reden = (String) afwezig.get("reden");
                    String cursusNaam = (String) afwezig.get("cursusNaam");
                    LocalDateTime begintijd = (LocalDateTime) afwezig.get("begintijd");
                    LocalDateTime eindtijd = (LocalDateTime) afwezig.get("eindtijd");
                    String studentnaam = afwezig.get("naam").toString();
                    LocalDate lesdatum = begintijd.toLocalDate();

                    if (overzichtDatePicker.getValue().isEqual(lesdatum)) {
                        String lesinfo;
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

    public void toonabsentlessenSLB(){
        try {
            int slbGID = parentController.getGebruikerID();
            ObservableList<String> dagafwezig = FXCollections.observableArrayList();

            for (Map<String, Object> getlesID : executeStatement("SELECT LesID FROM afwezigheid WHERE leerlingID =" + parentController.getGebruikerID())) {
                for (Map<String, Object> les : executeStatement("SELECT * FROM les WHERE lesID =" + getlesID.get("lesID") + " ORDER BY begintijd")) {
                    String vakNaam = "";

                    for (Map<String, Object> lesnaamq : executeStatement("SELECT cursusNaam FROM cursus WHERE cursusID = " + les.get("cursusID"))) {
                        vakNaam = lesnaamq.get("cursusNaam").toString();
                    }
                    LocalDateTime datumTijdBegin = (LocalDateTime) les.get("begintijd");
                    LocalDateTime datumTijdEind = (LocalDateTime) les.get("eindtijd");
                    LocalDate lesDatum = datumTijdBegin.toLocalDate();

                    if(overzichtDatePicker.getValue().isEqual(lesDatum)){
                        String lesinfo="afwezig voor ";
                        lesinfo = lesinfo + "Les : " + vakNaam;
                        lesinfo = lesinfo + " | tijd : " + datumTijdBegin.getHour() + "." + datumTijdBegin.getMinute() + " - " + datumTijdEind.getHour() + "." + datumTijdEind.getMinute();
                        dagafwezig.add(lesinfo);
                    } else {
                        dagafwezig.add("");
                    }
                }
            }
            aaanwezigList.setItems(dagafwezig);
            aanwezigCalcLabel.setText("" + overzichtDatePicker.getValue().getDayOfWeek().toString().toLowerCase() + ", " + overzichtDatePicker.getValue().getDayOfMonth() + "/" + overzichtDatePicker.getValue().getMonth().toString().toLowerCase() + "/" + overzichtDatePicker.getValue().getYear());
        } catch (NullPointerException e) {
            aanwezigCalcLabel.setText("Error! couldn't load the data");
        }
    }

    public void toonabsentlessen(){
        try {
            ObservableList<String> dagafwezig = FXCollections.observableArrayList();

            for (Map<String, Object> getlesID : executeStatement("SELECT LesID FROM afwezigheid WHERE leerlingID =" + parentController.getGebruikerID())) {
                for (Map<String, Object> les : executeStatement("SELECT * FROM les WHERE lesID =" + getlesID.get("lesID") + " ORDER BY begintijd")) {
			        String vakNaam = "";

			        for (Map<String, Object> lesnaamq : executeStatement("SELECT cursusNaam FROM cursus WHERE cursusID = " + les.get("cursusID"))) {
                        vakNaam = lesnaamq.get("cursusNaam").toString();
                    }
			        LocalDateTime datumTijdBegin = (LocalDateTime) les.get("begintijd");
			        LocalDateTime datumTijdEind = (LocalDateTime) les.get("eindtijd");
			        LocalDate lesDatum = datumTijdBegin.toLocalDate();

                    if(overzichtDatePicker.getValue().isEqual(lesDatum)){
                        String lesinfo="afwezig voor ";
                        lesinfo = lesinfo + "Les : " + vakNaam;
                        lesinfo = lesinfo + " | tijd : " + datumTijdBegin.getHour() + "." + datumTijdBegin.getMinute() + " - " + datumTijdEind.getHour() + "." + datumTijdEind.getMinute();
                    dagafwezig.add(lesinfo);
                    } else {
                        dagafwezig.add("");
                    }
                }
            }
            aaanwezigList.setItems(dagafwezig);
            aanwezigCalcLabel.setText("" + overzichtDatePicker.getValue().getDayOfWeek().toString().toLowerCase() + ", " + overzichtDatePicker.getValue().getDayOfMonth() + "/" + overzichtDatePicker.getValue().getMonth().toString().toLowerCase() + "/" + overzichtDatePicker.getValue().getYear());
        } catch (NullPointerException e) {
            aanwezigCalcLabel.setText("Error! couldn't load the data");
        }
    }
}
