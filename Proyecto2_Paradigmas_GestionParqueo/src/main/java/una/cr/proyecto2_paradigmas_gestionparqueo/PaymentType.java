/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package una.cr.proyecto2_paradigmas_gestionparqueo;

/**
 * Lists the supported payment methods.
 *
 * @author mcfra
 */
public enum PaymentType {
    /**
     * Payment in cash without a charge adjustment.
     */
    CASH,
    /**
     * Payment by card with a two percent surcharge.
     */
    CARD,
    /**
     * Payment by SINPE Mobile with a one percent discount.
     */
    SINPE_MOBILE
}
