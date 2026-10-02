/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package una.cr.proyecto2_paradigmas_gestionparqueo;

/**
 * Represents a card payment with a two percent surcharge.
 * Validation checks for a positive total and a nonblank authorization code;
 * it does not contact a payment provider.
 *
 * @author mcfra
 */
public class CardPayment extends PaymentMethod {

    private final String authorizationCode;

    /**
     * Creates a card payment with the supplied authorization code.
     *
     * @param authorizationCode the authorization code, checked during validation
     */
    public CardPayment(String authorizationCode) {
        super(PaymentType.CARD);
        this.authorizationCode = authorizationCode;
    }

    /**
     * Calculates a two percent surcharge using integer division.
     * Any fractional colones are discarded.
     *
     * @param baseAmount the base charge in colones
     * @return the adjustment in whole colones
     */
    @Override
    protected long calculateAdjustment(long baseAmount) {
        return baseAmount * 2 / 100;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validatePayment(long totalAmount) {
        return totalAmount > 0 && authorizationCode != null
                && !authorizationCode.isBlank();
    }

    /**
     * Returns the supplied authorization code.
     *
     * @return the supplied authorization code, which may be null
     */
    public String getAuthorizationCode() {
        return authorizationCode;
    }
}