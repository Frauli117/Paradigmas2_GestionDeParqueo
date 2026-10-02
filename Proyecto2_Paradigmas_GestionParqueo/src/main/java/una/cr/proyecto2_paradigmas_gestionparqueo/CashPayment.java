/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package una.cr.proyecto2_paradigmas_gestionparqueo;

/**
 * Validates cash payments and calculates change after successful validation.
 * Cash payments do not adjust the base charge.
 *
 * @author mcfra
 */
public class CashPayment extends PaymentMethod {

    private final long amountReceived;
    private long change;

    /**
     * Creates a cash payment with an initial change amount of zero.
     *
     * @param amountReceived the amount tendered in colones; checked during validation
     */
    public CashPayment(long amountReceived) {
        super(PaymentType.CASH);
        this.amountReceived = amountReceived;
    }

    /**
     * Returns zero because cash payments have no adjustment.
     *
     * @param baseAmount the base charge in colones
     * @return the adjustment in whole colones
     */
    @Override
    protected long calculateAdjustment(long baseAmount) {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validatePayment(long totalAmount) {
        if (totalAmount <= 0 || amountReceived < totalAmount) {
            return false;
        }

        change = amountReceived - totalAmount;
        return true;
    }

    /**
     * Returns the cash amount tendered.
     *
     * @return the amount tendered in colones
     */
    public long getAmountReceived() {
        return amountReceived;
    }

    /**
     * Returns the change calculated by the last successful payment validation.
     *
     * @return the change from the last successful validation, or zero before any successful validation
     */
    public long getChange() {
        return change;
    }
}
