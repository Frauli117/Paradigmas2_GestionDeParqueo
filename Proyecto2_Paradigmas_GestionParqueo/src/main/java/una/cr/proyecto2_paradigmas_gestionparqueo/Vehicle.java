/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package una.cr.proyecto2_paradigmas_gestionparqueo;

import java.util.Locale;
/**
 * Stores immutable vehicle identification data and defines space and rate requirements.
 * All text fields are trimmed, and license plates are converted to uppercase.
 *
 * @author mcfra
 */
public abstract class Vehicle {

    private final String licensePlate;
    private final String brand;
    private final String model;
    private final String color;

    /**
     * Creates a vehicle with trimmed identification data and an uppercase license plate.
     *
     * @param licensePlate the nonblank license plate
     * @param brand the nonblank brand
     * @param model the nonblank model
     * @param color the nonblank color
     * @throws IllegalArgumentException if any argument is null or blank
     */
    public Vehicle(String licensePlate, String brand, String model, String color) {
        this.licensePlate = requireText(licensePlate, "License plate")
                .toUpperCase(Locale.ROOT);
        this.brand = requireText(brand, "Brand");
        this.model = requireText(model, "Model");
        this.color = requireText(color, "Color");
    }

    /**
     * Validates and trims a required text field.
     *
     * @param value the text to validate
     * @param fieldName the field name used in the error message
     * @return the trimmed text
     * @throws IllegalArgumentException if the text is null or blank
     */
    private String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty.");
        }
        return value.trim();
    }

    /**
     * Returns the vehicle license plate.
     *
     * @return the trimmed uppercase license plate
     */
    public String getLicensePlate() {
        return licensePlate;
    }

    /**
     * Returns the vehicle brand.
     *
     * @return the trimmed brand
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Returns the vehicle model.
     *
     * @return the trimmed model
     */
    public String getModel() {
        return model;
    }

    /**
     * Returns the vehicle color.
     *
     * @return the trimmed color
     */
    public String getColor() {
        return color;
    }

    /**
     * Returns the vehicle space requirement.
     *
     * @return the compatible parking space type
     */
    public abstract SpaceType getRequiredSpaceType();

    /**
     * Returns the vehicle hourly rate.
     *
     * @return the hourly rate in colones
     */
    public abstract int getHourlyRate();

    /**
     * Returns the vehicle daily maximum.
     *
     * @return the daily maximum in colones
     */
    public abstract int getDailyMaximum();
}
