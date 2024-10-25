package entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import entities.annotations.*;
import services.xml.DateAdapter;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;

@XmlRootElement(name = "payment")
@Table(name = "payments")
public class Payment extends Entity {
    @JsonProperty
    @XmlElement
    @Id
    @Column(name = "job_id")
    public int jobId;

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @XmlElement
    @Column(name = "pay_date")
    @XmlJavaTypeAdapter(DateAdapter.class)
    public LocalDate payDate;

    @JsonProperty
    @XmlElement
    @Column(name = "amount")
    @Range(min = 1)
    public double amount;

    private Payment() {
    }

    public Payment(int jobId, LocalDate payDate, double amount) {
        this.jobId = jobId;
        this.payDate = payDate;
        this.amount = amount;
    }

    private Payment(Integer jobId, LocalDate payDate, Double amount) {
        this(jobId.intValue(), payDate, amount.doubleValue());
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public void setPayDate(LocalDate payDate) {
        this.payDate = payDate;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String toString() {
        return String.format("ID %d - %.2f", this.jobId, this.amount);
    }

}

