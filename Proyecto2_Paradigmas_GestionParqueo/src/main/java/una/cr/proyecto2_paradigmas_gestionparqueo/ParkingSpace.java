/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package una.cr.proyecto2_paradigmas_gestionparqueo;

/**
 * Represents a parking space, its availability, and its assigned vehicle.
 * A space accepts only vehicles matching its type.
 *
 * @author mcfra
 */
public class ParkingSpace {

    private final int number;
    private final String identifier;
    private final SpaceType type;
    private SpaceStatus status;
    private Vehicle assignedVehicle;

    /**
     * Creates an available parking space.
     *
     * @param number the positive space number
     * @param identifier the nonblank display identifier
     * @param type the supported vehicle category
     * @throws IllegalArgumentException if the number is not positive, the identifier is null or blank, or the type is null
     */
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

    /**
     * Checks whether this space is available and compatible with a vehicle.
     *
     * @param vehicle the vehicle to check, which may be null
     * @return true if the vehicle is nonnull and compatible and the space is available
     */
    public boolean canAssign(Vehicle vehicle) {
        return vehicle != null && status == SpaceStatus.AVAILABLE
                && type == vehicle.getRequiredSpaceType();
    }

    /**
     * Assigns a vehicle and marks this space as occupied.
     *
     * @param vehicle the vehicle to assign
     * @throws IllegalStateException if the vehicle is null or incompatible, or the space is unavailable
     */
    void occupy(Vehicle vehicle) {
        if (!canAssign(vehicle)) {
            throw new IllegalStateException(
                    "The space is unavailable or incompatible with the vehicle."
            );
        }

        assignedVehicle = vehicle;
        status = SpaceStatus.OCCUPIED;
    }

    /**
     * Clears the assigned vehicle and makes this space available.
     *
     * @throws IllegalStateException if the space is not occupied
     */
    void release() {
        if (status != SpaceStatus.OCCUPIED) {
            throw new IllegalStateException("The space is not occupied.");
        }

        assignedVehicle = null;
        status = SpaceStatus.AVAILABLE;
    }

    /**
     * Takes an available space out of service.
     *
     * @throws IllegalStateException if the space is not available
     */
    public void markOutOfService() {
        if (status != SpaceStatus.AVAILABLE) {
            throw new IllegalStateException(
                    "Only an available space can be taken out of service."
            );
        }

        status = SpaceStatus.OUT_OF_SERVICE;
    }

    /**
     * Makes a space that is out of service available again.
     *
     * @throws IllegalStateException if the space is not out of service
     */
    public void returnToService() {
        if (status != SpaceStatus.OUT_OF_SERVICE) {
            throw new IllegalStateException(
                    "The space is not out of service."
            );
        }

        status = SpaceStatus.AVAILABLE;
    }

    /**
     * Returns the space number.
     *
     * @return the positive space number
     */
    public int getNumber() {
        return number;
    }

    /**
     * Returns the display identifier.
     *
     * @return the trimmed display identifier
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Returns the supported vehicle category.
     *
     * @return the supported vehicle category
     */
    public SpaceType getType() {
        return type;
    }

    /**
     * Returns the current availability state.
     *
     * @return the current availability state
     */
    public SpaceStatus getStatus() {
        return status;
    }

    /**
     * Returns the vehicle currently assigned to this space.
     *
     * @return the assigned vehicle, or null if no vehicle is assigned
     */
    public Vehicle getAssignedVehicle() {
        return assignedVehicle;
    }
}
