package entities;

public class Payment {
    public int job_id;
    public java.sql.Date pay_date;
    public double amount;

    public Payment(int job_id, java.sql.Date pay_date, double amount) {
        this.job_id = job_id;
        this.pay_date = pay_date;
        this.amount = amount;
    }

    private Payment(Integer job_id, java.sql.Date pay_date, Double amount) {
        this(job_id.intValue(), pay_date, amount.doubleValue());
    }
}

