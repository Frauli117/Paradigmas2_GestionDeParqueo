/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package una.cr.proyecto2_paradigmas_gestionparqueo;

/**
 * Calculates parking charges in colones using vehicle rates and 24-hour blocks.
 * Each complete block uses the daily maximum. For remaining hours, the daily
 * maximum caps the hourly charge only when at least ten hours remain.
 *
 * @author mcfra
 */
public class ParkingRateRules {

    /**
     * Calculates the base charge for complete 24-hour blocks and remaining hours.
     * Remaining hours are capped at the daily maximum only when there are at least ten.
     *
     * @param vehicle the vehicle whose rates apply
     * @param chargedHours the positive number of whole hours to charge
     * @return the base parking charge in colones
     * @throws IllegalArgumentException if the vehicle is null or charged hours are not positive
     */
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
