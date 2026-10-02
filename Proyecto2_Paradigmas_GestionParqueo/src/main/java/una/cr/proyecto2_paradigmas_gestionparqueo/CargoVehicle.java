/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package una.cr.proyecto2_paradigmas_gestionparqueo;

/**
 * Represents a cargo vehicle requiring a cargo space, with a rate of
 * 1,500 colones per hour and a daily maximum of 11,000 colones.
 *
 * @author mcfra
 */
public class CargoVehicle extends Vehicle {

    /**
     * Creates a vehicle with trimmed identification data and an uppercase license plate.
     *
     * @param licensePlate the nonblank license plate
     * @param brand the nonblank brand
     * @param model the nonblank model
     * @param color the nonblank color
     * @throws IllegalArgumentException if any argument is null or blank
     */
    public CargoVehicle(String licensePlate, String brand, String model, String color) {
        super(licensePlate, brand, model, color);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SpaceType getRequiredSpaceType() {
        return SpaceType.CARGO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getHourlyRate() {
        return 1500;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDailyMaximum() {
        return 11000;
    }
}
