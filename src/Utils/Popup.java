package Utils;

import javafx.scene.control.Alert;

public abstract class Popup {
    public static void alert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Er is een fout opgetreden");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();}
}
