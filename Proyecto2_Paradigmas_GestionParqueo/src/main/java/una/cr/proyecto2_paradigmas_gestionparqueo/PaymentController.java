package una.cr.proyecto2_paradigmas_gestionparqueo;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * Handles ticket closure, payment details, and payment registration.
 */
public class PaymentController {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final ParkingService service = ParkingService.getInstance();

    @FXML
    private TableView<ParkingTicket> tblTickets;
    @FXML
    private TableColumn<ParkingTicket, Number> colTicket;
    @FXML
    private TableColumn<ParkingTicket, String> colPlate;
    @FXML
    private TableColumn<ParkingTicket, String> colSpace;
    @FXML
    private TableColumn<ParkingTicket, String> colEntry;
    @FXML
    private TableColumn<ParkingTicket, String> colStatus;
    @FXML
    private Label lblSelectedTicket;
    @FXML
    private Label lblBaseAmount;
    @FXML
    private Label lblTotalAmount;
    @FXML
    private ComboBox<String> cmbPaymentType;
    @FXML
    private Label lblData1;
    @FXML
    private Label lblData2;
    @FXML
    private TextField txtData1;
    @FXML
    private TextField txtData2;
    @FXML
    private Button btnCloseTicket;
    @FXML
    private Button btnPay;

    /**
     * Initializes view controls and displays the current service data.
     */
    @FXML
    private void initialize() {
        colTicket.setCellValueFactory(data ->
                new ReadOnlyIntegerWrapper(data.getValue().getNumber()));
        colPlate.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(data.getValue().getVehicle().getLicensePlate()));
        colSpace.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(data.getValue().getSpace().getIdentifier()));
        colEntry.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(data.getValue().getEntryTime().format(DATE_FORMAT)));
        colStatus.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(toSpanish(data.getValue().getStatus())));

        cmbPaymentType.setItems(FXCollections.observableArrayList(
                "Efectivo", "Tarjeta", "SINPE Móvil"
        ));
        cmbPaymentType.getSelectionModel().selectFirst();
        cmbPaymentType.setOnAction(event -> configurePaymentFields());

        tblTickets.getSelectionModel().selectedItemProperty().addListener(
                (observable, previous, selected) -> showTicket(selected)
        );

        configurePaymentFields();
        refreshTable();
        showTicket(null);
    }

    /**
     * Closes the selected active ticket and displays its hours and base charge.
     * Displays a warning when selection or ticket closure is invalid.
     */
    @FXML
    private void closeSelectedTicket() {
        ParkingTicket selected = tblTickets.getSelectionModel().getSelectedItem();
        if (selected == null) {
            UiUtils.warning("Seleccione un tiquete", "Seleccione un tiquete de la tabla.");
            return;
        }
        if (selected.getStatus() != TicketStatus.ACTIVE) {
            UiUtils.warning("Tiquete ya cerrado", "El tiquete seleccionado ya fue cerrado.");
            return;
        }

        try {
            service.closeTicket(selected.getNumber());
            refreshTable();
            selectTicket(selected.getNumber());
            UiUtils.information(
                    "Salida calculada",
                    "Monto base: ₡" + selected.getAmount()
                    + "\nHoras cobradas: " + selected.getChargedHours()
            );
        } catch (IllegalArgumentException | IllegalStateException ex) {
            UiUtils.warning("No se pudo cerrar el tiquete", ex.getMessage());
        }
    }

    /**
     * Registers payment for the selected closed ticket and refreshes pending tickets.
     * Displays the adjusted total and cash change, or a warning if payment fails.
     */
    @FXML
    private void paySelectedTicket() {
        ParkingTicket selected = tblTickets.getSelectionModel().getSelectedItem();
        if (selected == null) {
            UiUtils.warning("Seleccione un tiquete", "Seleccione un tiquete de la tabla.");
            return;
        }
        if (selected.getStatus() != TicketStatus.CLOSED) {
            UiUtils.warning(
                    "Pago no disponible",
                    "Primero cierre el tiquete para calcular la salida."
            );
            return;
        }

        try {
            PaymentMethod paymentMethod = createPaymentMethod();
            long total = paymentMethod.calculateTotal(selected.getAmount());
            lblTotalAmount.setText("₡" + total);

            long chargedTotal = service.processPayment(selected.getNumber(), paymentMethod);
            String extra = "";
            if (paymentMethod instanceof CashPayment) {
                extra = "\nCambio: ₡" + ((CashPayment) paymentMethod).getChange();
            }

            UiUtils.information(
                    "Pago registrado",
                    "Total cobrado: ₡" + chargedTotal + extra
                    + "\nEl espacio " + selected.getSpace().getIdentifier()
                    + " quedó disponible."
            );

            txtData1.clear();
            txtData2.clear();
            refreshTable();
            showTicket(null);
        } catch (NumberFormatException ex) {
            UiUtils.warning("Dato inválido", "El monto recibido debe ser un número entero.");
        } catch (IllegalArgumentException | IllegalStateException ex) {
            UiUtils.warning("No se pudo registrar el pago", ex.getMessage());
        }
    }

    /**
     * Creates a payment method using the selected type and entered details.
     *
     * @return the payment method described by the form
     * @throws IllegalArgumentException if no payment type is selected
     * @throws NumberFormatException if the cash amount cannot be parsed as a long integer
     */
    private PaymentMethod createPaymentMethod() {
        String paymentType = cmbPaymentType.getValue();
        if (paymentType == null) {
            throw new IllegalArgumentException("Seleccione un método de pago.");
        }

        switch (paymentType) {
            case "Tarjeta":
                return new CardPayment(txtData1.getText());
            case "SINPE Móvil":
                return new SinpeMobilePayment(txtData1.getText(), txtData2.getText());
            case "Efectivo":
            default:
                return new CashPayment(Long.parseLong(txtData1.getText().trim()));
        }
    }

    /**
     * Clears payment inputs and configures their labels and visibility for the selected method.
     */
    private void configurePaymentFields() {
        String paymentType = cmbPaymentType.getValue();
        txtData1.clear();
        txtData2.clear();

        if ("Tarjeta".equals(paymentType)) {
            lblData1.setText("Código de autorización");
            txtData1.setPromptText("Ej. AUT-45871");
            lblData2.setVisible(false);
            lblData2.setManaged(false);
            txtData2.setVisible(false);
            txtData2.setManaged(false);
        } else if ("SINPE Móvil".equals(paymentType)) {
            lblData1.setText("Número de teléfono");
            txtData1.setPromptText("Ej. 8888-8888");
            lblData2.setText("Número de referencia");
            txtData2.setPromptText("Ej. 784521");
            lblData2.setVisible(true);
            lblData2.setManaged(true);
            txtData2.setVisible(true);
            txtData2.setManaged(true);
        } else {
            lblData1.setText("Monto recibido (₡)");
            txtData1.setPromptText("Ej. 5000");
            lblData2.setVisible(false);
            lblData2.setManaged(false);
            txtData2.setVisible(false);
            txtData2.setManaged(false);
        }
    }

    /**
     * Updates ticket details and available actions for the current selection.
     *
     * @param ticket the selected ticket, or null to clear the details
     */
    private void showTicket(ParkingTicket ticket) {
        if (ticket == null) {
            lblSelectedTicket.setText("Ningún tiquete seleccionado");
            lblBaseAmount.setText("₡0");
            lblTotalAmount.setText("₡0");
            btnCloseTicket.setDisable(true);
            btnPay.setDisable(true);
            return;
        }

        lblSelectedTicket.setText(
                "Tiquete #" + ticket.getNumber()
                + " - " + ticket.getVehicle().getLicensePlate()
        );
        lblBaseAmount.setText("₡" + ticket.getAmount());
        lblTotalAmount.setText("₡" + ticket.getAmount());
        btnCloseTicket.setDisable(ticket.getStatus() != TicketStatus.ACTIVE);
        btnPay.setDisable(ticket.getStatus() != TicketStatus.CLOSED);
    }

    /**
     * Refreshes the table from the current parking service data.
     */
    private void refreshTable() {
        tblTickets.getItems().setAll(service.getPendingPaymentTickets());
    }

    /**
     * Selects a ticket in the table if its number is present.
     *
     * @param ticketNumber the number of the ticket to select
     */
    private void selectTicket(int ticketNumber) {
        for (ParkingTicket ticket : tblTickets.getItems()) {
            if (ticket.getNumber() == ticketNumber) {
                tblTickets.getSelectionModel().select(ticket);
                return;
            }
        }
    }

    /**
     * Converts a domain state to its Spanish display label.
     *
     * @param status the state to display
     * @return the Spanish state label
     */
    private String toSpanish(TicketStatus status) {
        switch (status) {
            case CLOSED:
                return "Cerrado / pendiente de pago";
            case PAID:
                return "Pagado";
            case ACTIVE:
            default:
                return "Activo";
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
