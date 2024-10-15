package entities;

import entities.annotations.*;

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @Column(name = "job_id", autoIncrement = true)
    public int jobId;

    @Column(name = "pay_date")
    public java.sql.Date payDate;

    @Column(name = "amount")
    @Range(min = 1)
    public double amount;

    public Payment(int jobId, java.sql.Date payDate, double amount) {
        this.jobId = jobId;
        this.payDate = payDate;
        this.amount = amount;
    }

    private Payment(Integer jobId, java.sql.Date payDate, Double amount) {
        this(jobId.intValue(), payDate, amount.doubleValue());
    }

    public String toString() {
        return String.format("ID %d - %.2f", this.jobId, this.amount);
    }

}

