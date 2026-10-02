package una.cr.proyecto2_paradigmas_gestionparqueo;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Provides modal JavaFX alerts with a title and message and no header.
 */
final class UiUtils {

    /**
     * Prevents instantiation of this utility class.
     */
    private UiUtils() {
    }

    /**
     * Shows a modal information alert and waits for it to close.
     *
     * @param title the alert title
     * @param message the alert content
     */
    static void information(String title, String message) {
        show(AlertType.INFORMATION, title, message);
    }

    /**
     * Shows a modal warning alert and waits for it to close.
     *
     * @param title the alert title
     * @param message the alert content
     */
    static void warning(String title, String message) {
        show(AlertType.WARNING, title, message);
    }

    /**
     * Shows a modal error alert and waits for it to close.
     *
     * @param title the alert title
     * @param message the alert content
     */
    static void error(String title, String message) {
        show(AlertType.ERROR, title, message);
    }

    /**
     * Creates a modal alert with no header and waits for it to close.
     *
     * @param type the alert type
     * @param title the alert title
     * @param message the alert content
     */
    private static void show(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
