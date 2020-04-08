import model.School;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

			for (Map<String, Object> lesnaamq : executeStatement("SELECT cursusNaam FROM cursus WHERE cursusID = " + les.get("cursusID")))
				vakNaam = lesnaamq.get("cursusNaam").toString();
			for (Map<String, Object> lesnaamq : executeStatement("SELECT naam FROM klas WHERE klasID = " + les.get("klasID")))
				klasNaam = lesnaamq.get("naam").toString();

			LocalDateTime begint = (LocalDateTime) les.get("begintijd");

			LocalDateTime eindt = (LocalDateTime) les.get("eindtijd");

			LocalDate lesdatum = begint.toLocalDate();

			LocalDateTime begintijd = begint;
			LocalDateTime eindtijd = eindt;

			nieuwSchool.voegLesToe(vakNaam, lesdatum, begintijd, eindtijd, klasNaam);
		}
		nieuwSchool.voegLesToe("OOP", LocalDate.now().plusDays(-111), LocalDateTime.now(),LocalDateTime.now(), "V1C");
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