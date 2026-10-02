package una.cr.proyecto2_paradigmas_gestionparqueo;

import java.io.IOException;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * Displays parking spaces and handles changes to their service availability.
 */
public class SpacesController {

    private final ParkingService service = ParkingService.getInstance();

    @FXML
    private TableView<ParkingSpace> tblSpaces;
    @FXML
    private TableColumn<ParkingSpace, Number> colNumber;
    @FXML
    private TableColumn<ParkingSpace, String> colIdentifier;
    @FXML
    private TableColumn<ParkingSpace, String> colType;
    @FXML
    private TableColumn<ParkingSpace, String> colStatus;
    @FXML
    private TableColumn<ParkingSpace, String> colVehicle;
    @FXML
    private Label lblAvailable;
    @FXML
    private Label lblOccupied;
    @FXML
    private Label lblOut;
    @FXML
    private Button btnOutOfService;
    @FXML
    private Button btnReturnToService;

    /**
     * Initializes view controls and displays the current service data.
     */
    @FXML
    private void initialize() {
        colNumber.setCellValueFactory(data ->
                new ReadOnlyIntegerWrapper(data.getValue().getNumber()));
        colIdentifier.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(data.getValue().getIdentifier()));
        colType.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(toSpanish(data.getValue().getType())));
        colStatus.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(toSpanish(data.getValue().getStatus())));
        colVehicle.setCellValueFactory(data -> {
            Vehicle vehicle = data.getValue().getAssignedVehicle();
            return new ReadOnlyStringWrapper(
                    vehicle == null ? "-" : vehicle.getLicensePlate()
            );
        });

        tblSpaces.getSelectionModel().selectedItemProperty().addListener(
                (observable, previous, selected) -> updateButtons(selected)
        );

        refreshTable();
        updateButtons(null);
    }

    /**
     * Takes the selected available space out of service and refreshes the view.
     * Displays a warning if the selection or state change is invalid.
     */
    @FXML
    private void markOutOfService() {
        ParkingSpace selected = tblSpaces.getSelectionModel().getSelectedItem();
        if (selected == null) {
            UiUtils.warning("Seleccione un espacio", "Seleccione un espacio de la tabla.");
            return;
        }

        try {
            service.markSpaceOutOfService(selected.getNumber());
            refreshTable();
            UiUtils.information("Espacio actualizado", "El espacio quedó fuera de servicio.");
        } catch (IllegalArgumentException | IllegalStateException ex) {
            UiUtils.warning("No se pudo actualizar", ex.getMessage());
        }
    }

    /**
     * Returns the selected space to service and refreshes the view.
     * Displays a warning if the selection or state change is invalid.
     */
    @FXML
    private void returnToService() {
        ParkingSpace selected = tblSpaces.getSelectionModel().getSelectedItem();
        if (selected == null) {
            UiUtils.warning("Seleccione un espacio", "Seleccione un espacio de la tabla.");
            return;
        }

        try {
            service.returnSpaceToService(selected.getNumber());
            refreshTable();
            UiUtils.information("Espacio actualizado", "El espacio volvió a estar disponible.");
        } catch (IllegalArgumentException | IllegalStateException ex) {
            UiUtils.warning("No se pudo actualizar", ex.getMessage());
        }
    }

    /**
     * Refreshes the table from the current parking service data.
     */
    private void refreshTable() {
        tblSpaces.getItems().setAll(service.getSpaces());
        lblAvailable.setText(String.valueOf(service.countSpaces(SpaceStatus.AVAILABLE)));
        lblOccupied.setText(String.valueOf(service.countSpaces(SpaceStatus.OCCUPIED)));
        lblOut.setText(String.valueOf(service.countSpaces(SpaceStatus.OUT_OF_SERVICE)));
        updateButtons(tblSpaces.getSelectionModel().getSelectedItem());
    }

    /**
     * Enables service actions according to the selected space state.
     *
     * @param selected the selected space, or null if none is selected
     */
    private void updateButtons(ParkingSpace selected) {
        boolean available = selected != null && selected.getStatus() == SpaceStatus.AVAILABLE;
        boolean out = selected != null && selected.getStatus() == SpaceStatus.OUT_OF_SERVICE;
        btnOutOfService.setDisable(!available);
        btnReturnToService.setDisable(!out);
    }

    /**
     * Converts a domain value to its Spanish display label.
     *
     * @param type the space category to display
     * @return the Spanish category label
     */
    private String toSpanish(SpaceType type) {
        switch (type) {
            case MOTORCYCLE:
                return "Motocicleta";
            case CARGO:
                return "Carga";
            case CAR:
            default:
                return "Automóvil";
        }
    }

    /**
     * Converts a domain state to its Spanish display label.
     *
     * @param status the state to display
     * @return the Spanish state label
     */
    private String toSpanish(SpaceStatus status) {
        switch (status) {
            case OCCUPIED:
                return "Ocupado";
            case OUT_OF_SERVICE:
                return "Fuera de servicio";
            case AVAILABLE:
            default:
                return "Disponible";
        }
    }

    /**
     * Opens the dashboard view.
     *
     * @throws IOException if the destination FXML view cannot be loaded
     */
    @FXML
    private void goBack() throws IOException {
        App.setRoot("dashboard");
    }
}
