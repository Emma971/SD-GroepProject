package src;

import rooster.model.School;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.LocalDate;

public class RoosterApp extends Application {
	public static void main(String[] args) throws Exception {
		School nieuwSchool = new School("HU");

		nieuwSchool.voegLesToe("OOP", LocalDate.now().plusDays(0),   "12:30","15:30", "V1C");
		nieuwSchool.voegLesToe("OOP", LocalDate.now().plusDays(1),   "12:30","15:30", "V1C");
		nieuwSchool.voegLesToe("OOP", LocalDate.now().plusDays(4),   "12:30","15:30", "V1C");
		nieuwSchool.voegLesToe("OOP", LocalDate.now().plusDays(5),   "12:30","15:30", "V1C");
		nieuwSchool.voegLesToe("OOP", LocalDate.now().plusDays(6),   "12:30","15:30", "V1C");
		nieuwSchool.voegLesToe("OOP", LocalDate.now().plusDays(7),   "12:30","15:30", "V1C");
		nieuwSchool.voegLesToe("OOP", LocalDate.now().plusDays(8),   "08:30","11:30", "V1C");
		nieuwSchool.voegLesToe("OOP", LocalDate.now().plusDays(9),   "08:30","11:30", "V1C");
		nieuwSchool.voegLesToe("OOP", LocalDate.now().plusDays(10),  "08:30","11:30", "V1C");
		nieuwSchool.voegLesToe("OOP", LocalDate.now().plusDays(13),  "08:30","11:30", "V1C");
		nieuwSchool.voegLesToe("OOP", LocalDate.now().plusDays(14),  "08:30","11:30", "V1C");

		nieuwSchool.voegLesToe("OOAD", LocalDate.now().plusDays(0),   "08:30","11:30", "V1C");
		nieuwSchool.voegLesToe("OOAD", LocalDate.now().plusDays(1),   "08:30","11:30", "V1C");
		nieuwSchool.voegLesToe("OOAD", LocalDate.now().plusDays(2),   "08:30","11:30", "V1C");
		nieuwSchool.voegLesToe("OOAD", LocalDate.now().plusDays(3),   "08:30","11:30", "V1C");
		nieuwSchool.voegLesToe("OOAD", LocalDate.now().plusDays(6),   "08:30","11:30", "V1C");
		nieuwSchool.voegLesToe("OOAD", LocalDate.now().plusDays(7),   "08:30","11:30", "V1C");
		nieuwSchool.voegLesToe("OOAD", LocalDate.now().plusDays(8),   "12:30","15:30", "V1C");
		nieuwSchool.voegLesToe("OOAD", LocalDate.now().plusDays(9),   "12:30","15:30", "V1C");
		nieuwSchool.voegLesToe("OOAD", LocalDate.now().plusDays(10),  "12:30","15:30", "V1C");
		nieuwSchool.voegLesToe("OOAD", LocalDate.now().plusDays(13),  "12:30","15:30", "V1C");
		nieuwSchool.voegLesToe("OOAD", LocalDate.now().plusDays(14),  "12:30","15:30", "V1C");

		School.setSchool(nieuwSchool);

		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		String fxmlPagina = "src/userinterface/SchoolOverzicht.fxml";
		FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPagina));
		Parent root = loader.load();

		stage.setTitle("Rooster");
		stage.setScene(new Scene(root));
		stage.show();
	}
}