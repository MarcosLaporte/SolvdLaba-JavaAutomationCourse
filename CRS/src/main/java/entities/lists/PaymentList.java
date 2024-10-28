package entities.lists;

import com.fasterxml.jackson.annotation.JsonProperty;
import entities.Payment;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "payments")
public class PaymentList implements IEntityList<Payment> {
    @JsonProperty("payments")
    private List<Payment> paymentList;

    private PaymentList() {
    }

    private PaymentList(List<Payment> paymentList) {
        this.paymentList = paymentList;
    }

    @XmlElement(name = "payment")
    @Override
    public List<Payment> getList() {
        return this.paymentList;
    }

    @Override
    public void setList(List<Payment> paymentList) {
        this.paymentList = paymentList;
    }
}
