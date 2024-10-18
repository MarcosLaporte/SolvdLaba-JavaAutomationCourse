package entities.lists;

import entities.Payment;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "payments")
public class PaymentList {
    private List<Payment> paymentList;

    @XmlElement(name = "payment")
    public List<Payment> getPaymentList() {
        return this.paymentList;
    }

    public void setPaymentList(List<Payment> paymentList) {
        this.paymentList = paymentList;
    }
}
