/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package una.cr.proyecto2_paradigmas_gestionparqueo;

/**
 *
 * @author mcfra
 */
public abstract class PaymentMethod {

    private final PaymentType type;

    protected PaymentMethod(PaymentType type) {
        if (type == null) {
            throw new IllegalArgumentException("Payment type is required.");
        }

        this.type = type;
    }

    public final PaymentType getType() {
        return type;
    }

    public final long calculateTotal(long baseAmount) {
        if (baseAmount <= 0) {
            throw new IllegalArgumentException("Base amount must be positive.");
        }

        return baseAmount + calculateAdjustment(baseAmount);
    }

    protected abstract long calculateAdjustment(long baseAmount);

    public abstract boolean validatePayment(long totalAmount);
}
