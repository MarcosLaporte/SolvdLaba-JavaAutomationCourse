package entities;

public class Invoice {
    public int inv_id;
    public int ticket_id;
    public int tech_id;
    public String diagnosis;
    public double amount;

    public Invoice(int inv_id, int ticket_id, int tech_id, String diagnosis, double amount) {
        this.inv_id = inv_id;
        this.ticket_id = ticket_id;
        this.tech_id = tech_id;
        this.diagnosis = diagnosis;
        this.amount = amount;
    }

    private Invoice(Integer inv_id, Integer ticket_id, Integer tech_id, String diagnosis, Double amount) {
        this(inv_id.intValue(), ticket_id.intValue(), tech_id.intValue(), diagnosis, amount.doubleValue());
    }
}
