/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package una.cr.proyecto2_paradigmas_gestionparqueo;

/**
 *
 * @author mcfra
 */
public class CashPayment extends PaymentMethod {

    private final long amountReceived;
    private long change;

    public CashPayment(long amountReceived) {
        super(PaymentType.CASH);
        this.amountReceived = amountReceived;
    }

    @Override
    protected long calculateAdjustment(long baseAmount) {
        return 0;
    }

    @Override
    public boolean validatePayment(long totalAmount) {
        if (totalAmount <= 0 || amountReceived < totalAmount) {
            return false;
        }

        change = amountReceived - totalAmount;
        return true;
    }

    public long getAmountReceived() {
        return amountReceived;
    }

    public long getChange() {
        return change;
    }
}
