package una.cr.proyecto2_paradigmas_gestionparqueo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Coordinates vehicle entries, ticket closure, payments, and space availability.
 * The shared instance stores data in memory and initially provides four motorcycle
 * spaces, eight car spaces, and three cargo spaces. Data is not persisted.
 */
public final class ParkingService {

    private static final ParkingService INSTANCE = new ParkingService();

    private final List<ParkingSpace> spaces = new ArrayList<>();
    private final List<ParkingTicket> tickets = new ArrayList<>();
    private final ParkingRateRules rateRules = new ParkingRateRules();
    private int nextTicketNumber = 1;

    /**
     * Creates the shared in-memory service and its default parking spaces.
     */
    private ParkingService() {
        createDefaultSpaces();
    }

    /**
     * Returns the shared parking service.
     *
     * @return the singleton service instance
     */
    public static ParkingService getInstance() {
        return INSTANCE;
    }

    /**
     * Creates four motorcycle spaces, eight car spaces, and three cargo spaces.
     */
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

    /**
     * Assigns the first compatible available space and creates an active ticket
     * using the current time. Duplicate plates are checked against active tickets.
     *
     * @param vehicle the vehicle entering the parking lot
     * @return the newly created ticket
     * @throws IllegalArgumentException if the vehicle is null
     * @throws IllegalStateException if its plate already has an active ticket or no compatible space is available
     */
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

    /**
     * Closes a ticket at the current time and calculates its base charge.
     * The parking space remains occupied until payment succeeds.
     *
     * @param ticketNumber the ticket number to close
     * @return the closed ticket
     * @throws IllegalArgumentException if the ticket does not exist or the current time is not after entry
     * @throws IllegalStateException if the ticket is not active
     */
    public synchronized ParkingTicket closeTicket(int ticketNumber) {
        ParkingTicket ticket = requireTicket(ticketNumber);
        ticket.close(LocalDateTime.now(), rateRules);
        return ticket;
    }

    /**
     * Validates payment for a closed ticket, marks it paid, and releases its space.
     *
     * @param ticketNumber the ticket number to pay
     * @param paymentMethod the payment method and supplied details
     * @return the total paid in colones, including the payment adjustment
     * @throws IllegalArgumentException if the method is null, the ticket is missing, the base charge is not positive, or payment validation fails
     * @throws IllegalStateException if the ticket is not closed or its space is not occupied
     */
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

    /**
     * Takes an available parking space out of service.
     *
     * @param spaceNumber the parking space number
     * @throws IllegalArgumentException if the space does not exist
     * @throws IllegalStateException if the space is not available
     */
    public synchronized void markSpaceOutOfService(int spaceNumber) {
        requireSpace(spaceNumber).markOutOfService();
    }

    /**
     * Returns a parking space to service.
     *
     * @param spaceNumber the parking space number
     * @throws IllegalArgumentException if the space does not exist
     * @throws IllegalStateException if the space is not out of service
     */
    public synchronized void returnSpaceToService(int spaceNumber) {
        requireSpace(spaceNumber).returnToService();
    }

    /**
     * Looks up a ticket by number.
     *
     * @param ticketNumber the ticket number to find
     * @return an optional containing the ticket, or an empty optional if not found
     */
    public synchronized Optional<ParkingTicket> findTicket(int ticketNumber) {
        return tickets.stream()
                .filter(ticket -> ticket.getNumber() == ticketNumber)
                .findFirst();
    }

    /**
     * Returns active tickets.
     * The contained objects remain mutable and reflect subsequent state changes.
     *
     * @return an unmodifiable snapshot containing active tickets
     */
    public synchronized List<ParkingTicket> getActiveTickets() {
        List<ParkingTicket> result = new ArrayList<>();
        for (ParkingTicket ticket : tickets) {
            if (ticket.getStatus() == TicketStatus.ACTIVE) {
                result.add(ticket);
            }
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * Returns tickets that still await payment.
     * The contained objects remain mutable and reflect subsequent state changes.
     *
     * @return an unmodifiable snapshot containing active and closed tickets
     */
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

    /**
     * Returns all registered tickets.
     * The contained objects remain mutable and reflect subsequent state changes.
     *
     * @return an unmodifiable snapshot of all tickets
     */
    public synchronized List<ParkingTicket> getTickets() {
        return Collections.unmodifiableList(new ArrayList<>(tickets));
    }

    /**
     * Returns all parking spaces.
     * The contained objects remain mutable and reflect subsequent state changes.
     *
     * @return an unmodifiable snapshot of all spaces
     */
    public synchronized List<ParkingSpace> getSpaces() {
        return Collections.unmodifiableList(new ArrayList<>(spaces));
    }

    /**
     * Counts spaces with the specified state.
     *
     * @param status the state to count; null matches no spaces
     * @return the number of matching spaces
     */
    public synchronized long countSpaces(SpaceStatus status) {
        return spaces.stream().filter(space -> space.getStatus() == status).count();
    }

    /**
     * Counts tickets with the specified state.
     *
     * @param status the state to count; null matches no tickets
     * @return the number of matching tickets
     */
    public synchronized long countTickets(TicketStatus status) {
        return tickets.stream().filter(ticket -> ticket.getStatus() == status).count();
    }

    /**
     * Finds a required ticket.
     *
     * @param ticketNumber the ticket number
     * @return the matching ticket
     * @throws IllegalArgumentException if the ticket does not exist
     */
    private ParkingTicket requireTicket(int ticketNumber) {
        return findTicket(ticketNumber)
                .orElseThrow(() -> new IllegalArgumentException(
                "No existe el tiquete #" + ticketNumber + "."
        ));
    }

    /**
     * Finds a required space.
     *
     * @param spaceNumber the space number
     * @return the matching space
     * @throws IllegalArgumentException if the space does not exist
     */
    private ParkingSpace requireSpace(int spaceNumber) {
        return spaces.stream()
                .filter(space -> space.getNumber() == spaceNumber)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                "No existe el espacio #" + spaceNumber + "."
        ));
    }
}
