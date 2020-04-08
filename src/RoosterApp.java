import model.School;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static Utils.Database.executeStatement;

public class RoosterApp extends Application {
	public static void main(String[] args) throws Exception {

		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy");
		School nieuwSchool = new School("HU");

		for (Map<String, Object> les : executeStatement("SELECT * FROM les ORDER BY begintijd")) {
			String vakNaam = "";
			String klasNaam = "";
			String rawDatum = "";
			String rawBeginTijd = "";
			String rawEindTijd = "";

			for (Map<String, Object> lesnaamq : executeStatement("SELECT cursusNaam FROM cursus WHERE cursusID = " + les.get("cursusID")))
				vakNaam = lesnaamq.get("cursusNaam").toString();
			for (Map<String, Object> lesnaamq : executeStatement("SELECT naam FROM klas WHERE klasID = " + les.get("klasID")))
				klasNaam = lesnaamq.get("naam").toString();

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

			nieuwSchool.voegLesToe(vakNaam, lesdatum, begintijd, eindtijd, klasNaam);
		}
		nieuwSchool.voegLesToe("OOP", LocalDate.now().plusDays(-111),   "12:30","15:30", "V1C");
		School.setSchool(nieuwSchool);
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		String fxmlPagina = "userinterface/Mainmenu.fxml";
		FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPagina));
		Parent root = loader.load();

		stage.setTitle("Presentie melden Progamma");
		stage.setScene(new Scene(root));
		stage.show();
	}
}