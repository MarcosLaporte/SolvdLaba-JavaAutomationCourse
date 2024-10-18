package entities.lists;

import entities.Payment;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "payments")
public class PaymentList {
    private List<Payment> payments;

    @XmlElement(name = "payment")
    public List<Payment> getPayments() {
        return this.payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }
}
