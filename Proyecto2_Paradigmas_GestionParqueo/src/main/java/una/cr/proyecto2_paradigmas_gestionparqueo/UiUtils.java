package una.cr.proyecto2_paradigmas_gestionparqueo;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

final class UiUtils {

    private UiUtils() {
    }

    static void information(String title, String message) {
        show(AlertType.INFORMATION, title, message);
    }

    static void warning(String title, String message) {
        show(AlertType.WARNING, title, message);
    }

    static void error(String title, String message) {
        show(AlertType.ERROR, title, message);
    }

    private static void show(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
