/**
 * Defines the parking management application module.
 * Exports the application package and opens it to JavaFX FXML loading.
 */
module una.cr.proyecto2_paradigmas_gestionparqueo {
    requires javafx.controls;
    requires javafx.fxml;

    opens una.cr.proyecto2_paradigmas_gestionparqueo to javafx.fxml;
    exports una.cr.proyecto2_paradigmas_gestionparqueo;
}
