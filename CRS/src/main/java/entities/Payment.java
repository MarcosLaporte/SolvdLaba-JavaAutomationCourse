package entities;

import entities.annotations.*;

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @Column(name = "job_id", autoIncrement = true)
    public int id;

    @Column(name = "pay_date")
    public java.sql.Date payDate;

    @Column(name = "amount")
    @Range(min = 1)
    public double amount;

    public Payment(int id, java.sql.Date payDate, double amount) {
        this.id = id;
        this.payDate = payDate;
        this.amount = amount;
    }

    private Payment(Integer id, java.sql.Date payDate, Double amount) {
        this(id.intValue(), payDate, amount.doubleValue());
    }

    public String toString() {
        return String.format("ID %d - %.2f", this.id, this.amount);
    }

}

