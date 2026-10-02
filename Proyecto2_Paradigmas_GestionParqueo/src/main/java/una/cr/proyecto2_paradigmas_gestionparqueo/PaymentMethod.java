/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package una.cr.proyecto2_paradigmas_gestionparqueo;

/**
 * Defines a payment type, its adjustment to the base charge, and payment validation.
 * Amounts are expressed in whole colones.
 *
 * @author mcfra
 */
public abstract class PaymentMethod {

    private final PaymentType type;

    /**
     * Creates a payment method of the specified type.
     *
     * @param type the payment type
     * @throws IllegalArgumentException if the type is null
     */
    protected PaymentMethod(PaymentType type) {
        if (type == null) {
            throw new IllegalArgumentException("Payment type is required.");
        }

        this.type = type;
    }

    /**
     * Returns this payment method type.
     *
     * @return the payment type
     */
    public final PaymentType getType() {
        return type;
    }

    /**
     * Adds the payment adjustment to a positive base charge.
     *
     * @param baseAmount the positive base charge in colones
     * @return the adjusted total in colones
     * @throws IllegalArgumentException if the base charge is not positive
     */
    public final long calculateTotal(long baseAmount) {
        if (baseAmount <= 0) {
            throw new IllegalArgumentException("Base amount must be positive.");
        }

        return baseAmount + calculateAdjustment(baseAmount);
    }

    /**
     * Calculates the amount to add to the base charge.
     * A negative value represents a discount.
     *
     * @param baseAmount the base charge in colones
     * @return the adjustment in colones
     */
    protected abstract long calculateAdjustment(long baseAmount);

    /**
     * Checks whether the supplied payment details are valid for the total.
     * Cash implementations also calculate change when validation succeeds.
     *
     * @param totalAmount the total to pay in colones
     * @return true if the total is positive and the payment details are sufficient; false otherwise
     */
    public abstract boolean validatePayment(long totalAmount);
}
