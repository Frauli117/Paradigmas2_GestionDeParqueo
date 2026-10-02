package una.cr.proyecto2_paradigmas_gestionparqueo;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * Handles vehicle entry forms and displays active parking tickets.
 */
public class EntryController {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final ParkingService service = ParkingService.getInstance();

    @FXML
    private ComboBox<String> cmbVehicleType;
    @FXML
    private TextField txtPlate;
    @FXML
    private TextField txtBrand;
    @FXML
    private TextField txtModel;
    @FXML
    private TextField txtColor;
    @FXML
    private TableView<ParkingTicket> tblActiveTickets;
    @FXML
    private TableColumn<ParkingTicket, Number> colTicket;
    @FXML
    private TableColumn<ParkingTicket, String> colPlate;
    @FXML
    private TableColumn<ParkingTicket, String> colType;
    @FXML
    private TableColumn<ParkingTicket, String> colSpace;
    @FXML
    private TableColumn<ParkingTicket, String> colEntry;

    /**
     * Initializes view controls and displays the current service data.
     */
    @FXML
    private void initialize() {
        cmbVehicleType.setItems(FXCollections.observableArrayList(
                "Automóvil", "Motocicleta", "Vehículo de carga"
        ));
        cmbVehicleType.getSelectionModel().selectFirst();

        colTicket.setCellValueFactory(data ->
                new ReadOnlyIntegerWrapper(data.getValue().getNumber()));
        colPlate.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(data.getValue().getVehicle().getLicensePlate()));
        colType.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(toSpanish(data.getValue().getVehicle().getRequiredSpaceType())));
        colSpace.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(data.getValue().getSpace().getIdentifier()));
        colEntry.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(data.getValue().getEntryTime().format(DATE_FORMAT)));

        refreshTable();
    }

    /**
     * Registers the vehicle entered in the form and refreshes active tickets.
     * Displays a warning if the form or parking assignment is invalid.
     */
    @FXML
    private void registerEntry() {
        try {
            Vehicle vehicle = createVehicle();
            ParkingTicket ticket = service.registerEntry(vehicle);

            UiUtils.information(
                    "Entrada registrada",
                    "Tiquete #" + ticket.getNumber()
                    + "\nEspacio asignado: " + ticket.getSpace().getIdentifier()
            );

            clearForm();
            refreshTable();
        } catch (IllegalArgumentException | IllegalStateException ex) {
            UiUtils.warning("No se pudo registrar la entrada", ex.getMessage());
        }
    }

    /**
     * Creates a vehicle using the selected category and entered form values.
     *
     * @return the vehicle described by the form
     * @throws IllegalArgumentException if no category is selected or a required vehicle field is null or blank
     */
    private Vehicle createVehicle() {
        String type = cmbVehicleType.getValue();
        String plate = txtPlate.getText();
        String brand = txtBrand.getText();
        String model = txtModel.getText();
        String color = txtColor.getText();

        if (type == null) {
            throw new IllegalArgumentException("Seleccione el tipo de vehículo.");
        }

        switch (type) {
            case "Motocicleta":
                return new Motorcycle(plate, brand, model, color);
            case "Vehículo de carga":
                return new CargoVehicle(plate, brand, model, color);
            case "Automóvil":
            default:
                return new Car(plate, brand, model, color);
        }
    }

    /**
     * Clears vehicle fields, selects the default vehicle type, and focuses the plate field.
     */
    private void clearForm() {
        txtPlate.clear();
        txtBrand.clear();
        txtModel.clear();
        txtColor.clear();
        cmbVehicleType.getSelectionModel().selectFirst();
        txtPlate.requestFocus();
    }

    /**
     * Refreshes the table from the current parking service data.
     */
    private void refreshTable() {
        tblActiveTickets.getItems().setAll(service.getActiveTickets());
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
     * Opens the dashboard view.
     *
     * @throws IOException if the destination FXML view cannot be loaded
     */
    @FXML
    private void goBack() throws IOException {
        App.setRoot("dashboard");
    }
}
