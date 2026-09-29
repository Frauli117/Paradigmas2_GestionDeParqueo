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

    private void refreshTable() {
        tblTickets.getItems().setAll(service.getPendingPaymentTickets());
    }

    private void selectTicket(int ticketNumber) {
        for (ParkingTicket ticket : tblTickets.getItems()) {
            if (ticket.getNumber() == ticketNumber) {
                tblTickets.getSelectionModel().select(ticket);
                return;
            }
        }
    }

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

    @FXML
    private void goBack() throws IOException {
        App.setRoot("dashboard");
    }
}
