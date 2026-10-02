package una.cr.proyecto2_paradigmas_gestionparqueo;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Starts the JavaFX parking management application and loads its views.
 */
public class App extends Application {

    private static Scene scene;

    /**
     * Loads the dashboard and displays the main application window.
     *
     * @param stage the primary JavaFX stage
     * @throws IOException if the dashboard FXML cannot be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("dashboard"), 1050, 700);
        stage.setTitle("Sistema de Gestión de Parqueo");
        stage.setMinWidth(900);
        stage.setMinHeight(620);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Replaces the current view with the specified FXML view.
     *
     * @param fxml the resource name without the .fxml extension
     * @throws IOException if the FXML view cannot be loaded
     */
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    /**
     * Loads a view from an FXML resource in the application package.
     *
     * @param fxml the resource name without the .fxml extension
     * @return the root node of the loaded view
     * @throws IOException if the FXML resource cannot be read or loaded
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    /**
     * Launches the JavaFX application.
     *
     * @param args the command-line arguments; not forwarded to the JavaFX launcher
     */
    public static void main(String[] args) {
        launch();
    }
}
