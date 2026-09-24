/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package una.cr.proyecto2_paradigmas_gestionparqueo;

/**
 *
 * @author mcfra
 */
public class ParkingSpace {

    private final int number;
    private final String identifier;
    private final SpaceType type;
    private SpaceStatus status;
    private Vehicle assignedVehicle;

    public ParkingSpace(int number, String identifier, SpaceType type) {
        if (number <= 0) {
            throw new IllegalArgumentException("Space number must be positive.");
        }
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException("Space identifier cannot be empty.");
        }
        if (type == null) {
            throw new IllegalArgumentException("Space type is required.");
        }

        this.number = number;
        this.identifier = identifier.trim();
        this.type = type;
        this.status = SpaceStatus.AVAILABLE;
    }

    public boolean canAssign(Vehicle vehicle) {
        return vehicle != null && status == SpaceStatus.AVAILABLE
                && type == vehicle.getRequiredSpaceType();
    }

    void occupy(Vehicle vehicle) {
        if (!canAssign(vehicle)) {
            throw new IllegalStateException(
                    "The space is unavailable or incompatible with the vehicle."
            );
        }

        assignedVehicle = vehicle;
        status = SpaceStatus.OCCUPIED;
    }

    void release() {
        if (status != SpaceStatus.OCCUPIED) {
            throw new IllegalStateException("The space is not occupied.");
        }

        assignedVehicle = null;
        status = SpaceStatus.AVAILABLE;
    }

    public void markOutOfService() {
        if (status != SpaceStatus.AVAILABLE) {
            throw new IllegalStateException(
                    "Only an available space can be taken out of service."
            );
        }

        status = SpaceStatus.OUT_OF_SERVICE;
    }

    public void returnToService() {
        if (status != SpaceStatus.OUT_OF_SERVICE) {
            throw new IllegalStateException(
                    "The space is not out of service."
            );
        }

        status = SpaceStatus.AVAILABLE;
    }

    public int getNumber() {
        return number;
    }

    public String getIdentifier() {
        return identifier;
    }

    public SpaceType getType() {
        return type;
    }

    public SpaceStatus getStatus() {
        return status;
    }

    public Vehicle getAssignedVehicle() {
        return assignedVehicle;
    }
}
