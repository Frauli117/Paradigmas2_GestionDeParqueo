/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package una.cr.proyecto2_paradigmas_gestionparqueo;
import java.time.Duration;
import java.time.LocalDateTime;
/**
 * Records a parking stay and its progression from active to closed to paid.
 * Closing a ticket rounds any partial hour up and calculates the base amount.
 * The assigned space remains occupied until payment is processed by the service.
 *
 * @author mcfra
 */
public class ParkingTicket {

    private final int number;
    private final Vehicle vehicle;
    private final ParkingSpace space;
    private final LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private TicketStatus status;
    private long chargedHours;
    private long amount;

    /**
     * Creates an active ticket with no exit time and zero charged hours and amount.
     *
     * @param number the positive ticket number
     * @param vehicle the associated vehicle
     * @param space the assigned parking space
     * @param entryTime the entry date and time
     * @throws IllegalArgumentException if the number is not positive or any reference argument is null
     */
    ParkingTicket(int number, Vehicle vehicle, ParkingSpace space,
            LocalDateTime entryTime
    ) {
        if (number <= 0 || vehicle == null || space == null || entryTime == null) {
            throw new IllegalArgumentException("Invalid ticket information.");
        }

        this.number = number;
        this.vehicle = vehicle;
        this.space = space;
        this.entryTime = entryTime;
        this.status = TicketStatus.ACTIVE;
    }

    /**
     * Closes an active ticket and calculates its base charge.
     * Any partial hour is rounded up to the next whole hour.
     *
     * @param exitTime the exit time, strictly after entry
     * @param rateRules the rules used to calculate the charge
     * @throws IllegalStateException if the ticket is not active
     * @throws IllegalArgumentException if the exit time is null or not after entry, or the rules are null
     */
    void close(LocalDateTime exitTime, ParkingRateRules rateRules) {
        if (status != TicketStatus.ACTIVE) {
            throw new IllegalStateException("Only an active ticket can be closed.");
        }
        if (exitTime == null || !exitTime.isAfter(entryTime)) {
            throw new IllegalArgumentException(
                    "Exit time must be after entry time."
            );
        }
        if (rateRules == null) {
            throw new IllegalArgumentException("Rate rules are required.");
        }

        Duration stay = Duration.between(entryTime, exitTime);
        long completeHours = stay.toHours();
        boolean hasFraction = !stay.minusHours(completeHours).isZero();
        long calculatedHours = completeHours + (hasFraction ? 1 : 0);
        long calculatedAmount = rateRules.calculateAmount(
                vehicle, calculatedHours
        );

        this.exitTime = exitTime;
        this.chargedHours = calculatedHours;
        this.amount = calculatedAmount;
        this.status = TicketStatus.CLOSED;
    }

    /**
     * Marks a closed ticket as paid.
     *
     * @throws IllegalStateException if the ticket is not closed
     */
    void markPaid() {
        if (status != TicketStatus.CLOSED) {
            throw new IllegalStateException("Only a closed ticket can be paid.");
        }

        status = TicketStatus.PAID;
    }

    /**
     * Returns the ticket number.
     *
     * @return the positive ticket number
     */
    public int getNumber() {
        return number;
    }

    /**
     * Returns the vehicle associated with this ticket.
     *
     * @return the associated vehicle
     */
    public Vehicle getVehicle() {
        return vehicle;
    }

    /**
     * Returns the space assigned to this stay.
     *
     * @return the assigned parking space
     */
    public ParkingSpace getSpace() {
        return space;
    }

    /**
     * Returns the entry date and time.
     *
     * @return the entry date and time
     */
    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    /**
     * Returns the exit date and time recorded when the ticket was closed.
     *
     * @return the exit date and time, or null while active
     */
    public LocalDateTime getExitTime() {
        return exitTime;
    }

    /**
     * Returns the current ticket state.
     *
     * @return the current ticket state
     */
    public TicketStatus getStatus() {
        return status;
    }

    /**
     * Returns the number of hours calculated when the ticket was closed.
     *
     * @return the rounded-up stay in hours, or zero while active
     */
    public long getChargedHours() {
        return chargedHours;
    }

    /**
     * Returns the base parking charge before payment adjustments.
     *
     * @return the base parking charge in colones before payment adjustments, or zero while active
     */
    public long getAmount() {
        return amount;
    }
}
