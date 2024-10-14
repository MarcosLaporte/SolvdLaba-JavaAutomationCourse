package entities;

import entities.annotations.*;

@Entity
@Table(name = "invoices")
public class Invoice {
    @Id
    @Column(name = "inv_id", autoIncrement = true)
    public int id;

    @Column(name = "ticket_id")
    public int ticketId;

    @Column(name = "tech_id")
    public int techId;

    @Column(name = "diagnosis")
    public String diagnosis;

    @Column(name = "amount")
    public double amount;

    public Invoice(int id, int ticketId, int techId, @Size(min = 1, max = 255) String diagnosis, @Range(min = 1) double amount) {
        this.id = id;
        this.ticketId = ticketId;
        this.techId = techId;
        this.diagnosis = diagnosis;
        this.amount = amount;
    }

    private Invoice(Integer id, Integer ticketId, Integer techId, String diagnosis, Double amount) {
        this(id.intValue(), ticketId.intValue(), techId.intValue(), diagnosis, amount.doubleValue());
    }

    public String toString() {
        return String.format("ID %d - %s", this.id, this.diagnosis);
    }

}