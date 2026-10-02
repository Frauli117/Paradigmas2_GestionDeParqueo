/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package una.cr.proyecto2_paradigmas_gestionparqueo;

/**
 * Lists the stages of a parking ticket.
 *
 * @author mcfra
 */
public enum TicketStatus {
    /**
     * Open parking stay with no calculated exit charge.
     */
    ACTIVE,
    /**
     * Stay closed and charge calculated, awaiting payment.
     */
    CLOSED,
    /**
     * Payment registered for the closed stay.
     */
    PAID
}
