/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package una.cr.proyecto2_paradigmas_gestionparqueo;

/**
 *
 * @author mcfra
 */
public class Car extends Vehicle {

    public Car(String licensePlate, String brand, String model, String color) {
        super(licensePlate, brand, model, color);
    }

    @Override
    public SpaceType getRequiredSpaceType() {
        return SpaceType.CAR;
    }

    @Override
    public int getHourlyRate() {
        return 900;
    }

    @Override
    public int getDailyMaximum() {
        return 7000;
    }
}
