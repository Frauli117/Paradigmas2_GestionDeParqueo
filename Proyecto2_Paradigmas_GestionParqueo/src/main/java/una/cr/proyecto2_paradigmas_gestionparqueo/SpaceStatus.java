/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package una.cr.proyecto2_paradigmas_gestionparqueo;

/**
 * Lists the availability states of a parking space.
 *
 * @author mcfra
 */
public enum SpaceStatus {
    /**
     * Available for assignment to a compatible vehicle.
     */
    AVAILABLE,
    /**
     * Assigned to a vehicle until its ticket is paid.
     */
    OCCUPIED,
    /**
     * Unavailable for vehicle assignment.
     */
    OUT_OF_SERVICE
}
