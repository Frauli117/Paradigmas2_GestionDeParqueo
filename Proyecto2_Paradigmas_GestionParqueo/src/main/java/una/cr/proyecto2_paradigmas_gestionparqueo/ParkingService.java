package una.cr.proyecto2_paradigmas_gestionparqueo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Coordinates the parking lot use cases. The domain classes keep their own
 * rules and this service connects them with the JavaFX controllers.
 */
public final class ParkingService {

    private static final ParkingService INSTANCE = new ParkingService();

    private final List<ParkingSpace> spaces = new ArrayList<>();
    private final List<ParkingTicket> tickets = new ArrayList<>();
    private final ParkingRateRules rateRules = new ParkingRateRules();
    private int nextTicketNumber = 1;

    private ParkingService() {
        createDefaultSpaces();
    }

    public static ParkingService getInstance() {
        return INSTANCE;
    }

    private void createDefaultSpaces() {
        int number = 1;

        for (int i = 1; i <= 4; i++) {
            spaces.add(new ParkingSpace(number++, "M-" + i, SpaceType.MOTORCYCLE));
        }
        for (int i = 1; i <= 8; i++) {
            spaces.add(new ParkingSpace(number++, "A-" + i, SpaceType.CAR));
        }
        for (int i = 1; i <= 3; i++) {
            spaces.add(new ParkingSpace(number++, "C-" + i, SpaceType.CARGO));
        }
    }

    public synchronized ParkingTicket registerEntry(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle is required.");
        }

        boolean alreadyInside = tickets.stream()
                .anyMatch(ticket -> ticket.getStatus() == TicketStatus.ACTIVE
                && ticket.getVehicle().getLicensePlate()
                        .equalsIgnoreCase(vehicle.getLicensePlate()));

        if (alreadyInside) {
            throw new IllegalStateException(
                    "Ya existe un vehículo dentro del parqueo con esa placa."
            );
        }

        ParkingSpace space = spaces.stream()
                .filter(candidate -> candidate.canAssign(vehicle))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                "No hay espacios disponibles para este tipo de vehículo."
        ));

        space.occupy(vehicle);
        ParkingTicket ticket = new ParkingTicket(
                nextTicketNumber++, vehicle, space, LocalDateTime.now()
        );
        tickets.add(ticket);
        return ticket;
    }

    public synchronized ParkingTicket closeTicket(int ticketNumber) {
        ParkingTicket ticket = requireTicket(ticketNumber);
        ticket.close(LocalDateTime.now(), rateRules);
        return ticket;
    }

    public synchronized long processPayment(int ticketNumber, PaymentMethod paymentMethod) {
        if (paymentMethod == null) {
            throw new IllegalArgumentException("Payment method is required.");
        }

        ParkingTicket ticket = requireTicket(ticketNumber);
        if (ticket.getStatus() != TicketStatus.CLOSED) {
            throw new IllegalStateException(
                    "El tiquete debe estar cerrado antes de registrar el pago."
            );
        }

        long total = paymentMethod.calculateTotal(ticket.getAmount());
        if (!paymentMethod.validatePayment(total)) {
            throw new IllegalArgumentException(
                    "Los datos del pago no son válidos o el monto es insuficiente."
            );
        }

        ticket.markPaid();
        ticket.getSpace().release();
        return total;
    }

    public synchronized void markSpaceOutOfService(int spaceNumber) {
        requireSpace(spaceNumber).markOutOfService();
    }

    public synchronized void returnSpaceToService(int spaceNumber) {
        requireSpace(spaceNumber).returnToService();
    }

    public synchronized Optional<ParkingTicket> findTicket(int ticketNumber) {
        return tickets.stream()
                .filter(ticket -> ticket.getNumber() == ticketNumber)
                .findFirst();
    }

    public synchronized List<ParkingTicket> getActiveTickets() {
        List<ParkingTicket> result = new ArrayList<>();
        for (ParkingTicket ticket : tickets) {
            if (ticket.getStatus() == TicketStatus.ACTIVE) {
                result.add(ticket);
            }
        }
        return Collections.unmodifiableList(result);
    }

    public synchronized List<ParkingTicket> getPendingPaymentTickets() {
        List<ParkingTicket> result = new ArrayList<>();
        for (ParkingTicket ticket : tickets) {
            if (ticket.getStatus() == TicketStatus.ACTIVE
                    || ticket.getStatus() == TicketStatus.CLOSED) {
                result.add(ticket);
            }
        }
        return Collections.unmodifiableList(result);
    }

    public synchronized List<ParkingTicket> getTickets() {
        return Collections.unmodifiableList(new ArrayList<>(tickets));
    }

    public synchronized List<ParkingSpace> getSpaces() {
        return Collections.unmodifiableList(new ArrayList<>(spaces));
    }

    public synchronized long countSpaces(SpaceStatus status) {
        return spaces.stream().filter(space -> space.getStatus() == status).count();
    }

    public synchronized long countTickets(TicketStatus status) {
        return tickets.stream().filter(ticket -> ticket.getStatus() == status).count();
    }

    private ParkingTicket requireTicket(int ticketNumber) {
        return findTicket(ticketNumber)
                .orElseThrow(() -> new IllegalArgumentException(
                "No existe el tiquete #" + ticketNumber + "."
        ));
    }

    private ParkingSpace requireSpace(int spaceNumber) {
        return spaces.stream()
                .filter(space -> space.getNumber() == spaceNumber)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                "No existe el espacio #" + spaceNumber + "."
        ));
    }
}
