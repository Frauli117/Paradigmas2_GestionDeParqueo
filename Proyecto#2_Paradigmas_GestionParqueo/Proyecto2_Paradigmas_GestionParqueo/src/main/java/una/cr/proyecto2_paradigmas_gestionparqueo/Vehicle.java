/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package una.cr.proyecto2_paradigmas_gestionparqueo;

import java.util.Locale;
/**
 *
 * @author mcfra
 */
public abstract class Vehicle {

    private final String licensePlate;
    private final String brand;
    private final String model;
    private final String color;

    public Vehicle(String licensePlate, String brand, String model, String color) {
        this.licensePlate = requireText(licensePlate, "License plate")
                .toUpperCase(Locale.ROOT);
        this.brand = requireText(brand, "Brand");
        this.model = requireText(model, "Model");
        this.color = requireText(color, "Color");
    }

    private String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty.");
        }
        return value.trim();
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getColor() {
        return color;
    }

    public abstract SpaceType getRequiredSpaceType();

    public abstract int getHourlyRate();

    public abstract int getDailyMaximum();
}
