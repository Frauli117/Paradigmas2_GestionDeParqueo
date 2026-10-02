/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package una.cr.proyecto2_paradigmas_gestionparqueo;

/**
 * Represents a SINPE Mobile payment with a one percent discount.
 * Validation checks for a positive total, a nonblank phone number, and a nonblank
 * reference number; it does not verify an external transfer.
 *
 * @author mcfra
 */
public class SinpeMobilePayment extends PaymentMethod {

    private final String phoneNumber;
    private final String referenceNumber;

    /**
     * Creates a SINPE Mobile payment with the supplied transfer details.
     *
     * @param phoneNumber the phone number, checked during validation
     * @param referenceNumber the transfer reference, checked during validation
     */
    public SinpeMobilePayment(String phoneNumber, String referenceNumber) {
        super(PaymentType.SINPE_MOBILE);
        this.phoneNumber = phoneNumber;
        this.referenceNumber = referenceNumber;
    }

    /**
     * Calculates a one percent discount using integer division.
     * Any fractional colones in the discount are discarded.
     *
     * @param baseAmount the base charge in colones
     * @return the adjustment in whole colones
     */
    @Override
    protected long calculateAdjustment(long baseAmount) {
        return -(baseAmount / 100);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validatePayment(long totalAmount) {
        return totalAmount > 0 && phoneNumber != null && !phoneNumber.isBlank()
                && referenceNumber != null
                && !referenceNumber.isBlank();
    }

    /**
     * Returns the supplied phone number.
     *
     * @return the supplied phone number, which may be null
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Returns the supplied transfer reference.
     *
     * @return the supplied transfer reference, which may be null
     */
    public String getReferenceNumber() {
        return referenceNumber;
    }
}
