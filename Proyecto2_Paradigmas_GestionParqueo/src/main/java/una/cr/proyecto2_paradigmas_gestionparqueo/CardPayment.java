/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package una.cr.proyecto2_paradigmas_gestionparqueo;

/**
 *
 * @author mcfra
 */
public class CardPayment extends PaymentMethod {

    private final String authorizationCode;

    public CardPayment(String authorizationCode) {
        super(PaymentType.CARD);
        this.authorizationCode = authorizationCode;
    }

    @Override
    protected long calculateAdjustment(long baseAmount) {
        return baseAmount * 2 / 100;
    }

    @Override
    public boolean validatePayment(long totalAmount) {
        return totalAmount > 0 && authorizationCode != null
                && !authorizationCode.isBlank();
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }
}