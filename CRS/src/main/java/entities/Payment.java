package entities;

import entities.annotations.*;

import java.time.LocalDate;

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @Column(name = "job_id")
    public int jobId;

    @Column(name = "pay_date")
    public LocalDate payDate;

    @Column(name = "amount")
    @Range(min = 1)
    public double amount;

    private Payment() {}

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

