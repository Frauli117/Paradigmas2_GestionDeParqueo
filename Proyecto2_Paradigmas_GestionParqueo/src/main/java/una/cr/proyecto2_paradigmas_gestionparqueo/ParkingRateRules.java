/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package una.cr.proyecto2_paradigmas_gestionparqueo;

/**
 *
 * @author mcfra
 */
public class ParkingRateRules {

    public long calculateAmount(Vehicle vehicle, long chargedHours) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle is required.");
        }
        if (chargedHours <= 0) {
            throw new IllegalArgumentException("Charged hours must be positive.");
        }

        long completeDays = chargedHours / 24;
        long remainingHours = chargedHours % 24;
        long total = completeDays * vehicle.getDailyMaximum();

        if (remainingHours > 0) {
            long hourlyAmount = remainingHours * vehicle.getHourlyRate();

            if (remainingHours >= 10) {
                total += Math.min(hourlyAmount, vehicle.getDailyMaximum());
            } else {
                total += hourlyAmount;
            }
        }

        return total;
    }
}
