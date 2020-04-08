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
		School nieuwSchool = new School("HU");

		for (Map<String, Object> les : executeStatement("SELECT les.begintijd, les.eindtijd, cursus.cursusNaam, klas.klasNaam FROM les INNER JOIN cursus ON les.cursusID = cursus.cursusID INNER JOIN klas ON les.klasID = klas.klasID ORDER BY les.begintijd")) {
			String vakNaam = (String)les.get("cursusNaam");
			String klasNaam = (String)les.get("naam");

			LocalDateTime begintijd = (LocalDateTime) les.get("begintijd");

			LocalDateTime eindtijd = (LocalDateTime) les.get("eindtijd");

			LocalDate lesdatum = begintijd.toLocalDate();

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