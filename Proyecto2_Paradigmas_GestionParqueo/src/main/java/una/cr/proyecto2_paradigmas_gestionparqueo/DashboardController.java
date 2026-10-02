package una.cr.proyecto2_paradigmas_gestionparqueo;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Displays parking availability and active ticket counts and opens the main views.
 */
public class DashboardController {

    private final ParkingService service = ParkingService.getInstance();

    @FXML
    private Label lblAvailable;
    @FXML
    private Label lblOccupied;
    @FXML
    private Label lblOutOfService;
    @FXML
    private Label lblActiveTickets;

    /**
     * Initializes view controls and displays the current service data.
     */
    @FXML
    private void initialize() {
        refreshSummary();
    }

    /**
     * Updates the displayed space counts and active ticket count.
     */
    @FXML
    private void refreshSummary() {
        lblAvailable.setText(String.valueOf(service.countSpaces(SpaceStatus.AVAILABLE)));
        lblOccupied.setText(String.valueOf(service.countSpaces(SpaceStatus.OCCUPIED)));
        lblOutOfService.setText(String.valueOf(service.countSpaces(SpaceStatus.OUT_OF_SERVICE)));
        lblActiveTickets.setText(String.valueOf(service.countTickets(TicketStatus.ACTIVE)));
    }

    /**
     * Opens the entry view.
     *
     * @throws IOException if the destination FXML view cannot be loaded
     */
    @FXML
    private void openEntry() throws IOException {
        App.setRoot("entry");
    }

    /**
     * Opens the payment view.
     *
     * @throws IOException if the destination FXML view cannot be loaded
     */
    @FXML
    private void openPayment() throws IOException {
        App.setRoot("payment");
    }

    /**
     * Opens the spaces view.
     *
     * @throws IOException if the destination FXML view cannot be loaded
     */
    @FXML
    private void openSpaces() throws IOException {
        App.setRoot("spaces");
    }
}
