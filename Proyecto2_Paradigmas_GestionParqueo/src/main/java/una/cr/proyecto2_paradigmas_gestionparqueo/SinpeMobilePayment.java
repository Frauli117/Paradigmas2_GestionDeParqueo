/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package una.cr.proyecto2_paradigmas_gestionparqueo;

/**
 *
 * @author mcfra
 */
public class SinpeMobilePayment extends PaymentMethod {

    private final String phoneNumber;
    private final String referenceNumber;

    public SinpeMobilePayment(String phoneNumber, String referenceNumber) {
        super(PaymentType.SINPE_MOBILE);
        this.phoneNumber = phoneNumber;
        this.referenceNumber = referenceNumber;
    }

    @Override
    protected long calculateAdjustment(long baseAmount) {
        return -(baseAmount / 100);
    }

    @Override
    public boolean validatePayment(long totalAmount) {
        return totalAmount > 0 && phoneNumber != null && !phoneNumber.isBlank()
                && referenceNumber != null
                && !referenceNumber.isBlank();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }
}
