package una.cr.proyecto2_paradigmas_gestionparqueo;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

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

    @FXML
    private void initialize() {
        refreshSummary();
    }

    @FXML
    private void refreshSummary() {
        lblAvailable.setText(String.valueOf(service.countSpaces(SpaceStatus.AVAILABLE)));
        lblOccupied.setText(String.valueOf(service.countSpaces(SpaceStatus.OCCUPIED)));
        lblOutOfService.setText(String.valueOf(service.countSpaces(SpaceStatus.OUT_OF_SERVICE)));
        lblActiveTickets.setText(String.valueOf(service.countTickets(TicketStatus.ACTIVE)));
    }

    @FXML
    private void openEntry() throws IOException {
        App.setRoot("entry");
    }

    @FXML
    private void openPayment() throws IOException {
        App.setRoot("payment");
    }

    @FXML
    private void openSpaces() throws IOException {
        App.setRoot("spaces");
    }
}
