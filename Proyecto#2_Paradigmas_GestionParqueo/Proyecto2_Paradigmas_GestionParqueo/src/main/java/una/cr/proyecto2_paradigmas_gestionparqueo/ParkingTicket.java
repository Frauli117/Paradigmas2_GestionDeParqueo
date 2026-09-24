/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package una.cr.proyecto2_paradigmas_gestionparqueo;
import java.time.Duration;
import java.time.LocalDateTime;
/**
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

    void markPaid() {
        if (status != TicketStatus.CLOSED) {
            throw new IllegalStateException("Only a closed ticket can be paid.");
        }

        status = TicketStatus.PAID;
    }

    public int getNumber() {
        return number;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public ParkingSpace getSpace() {
        return space;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public long getChargedHours() {
        return chargedHours;
    }

    public long getAmount() {
        return amount;
    }
}
