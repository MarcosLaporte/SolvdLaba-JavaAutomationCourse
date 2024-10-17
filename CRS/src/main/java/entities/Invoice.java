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
    @Size(min = 1, max = 255)
    public String diagnosis;

    @Column(name = "amount")
    @Range(min = 1)
    public double amount;

    public Invoice(int ticketId, int techId, String diagnosis, double amount) {
        this.ticketId = ticketId;
        this.techId = techId;
        this.diagnosis = diagnosis;
        this.amount = amount;
    }

    private Invoice(Integer ticketId, Integer techId, String diagnosis, Double amount) {
        this(ticketId.intValue(), techId.intValue(), diagnosis, amount.doubleValue());
    }

    public Invoice(int id, int ticketId, int techId, String diagnosis, double amount) {
        this(ticketId, techId, diagnosis, amount);
        this.id = id;
    }

    private Invoice(Integer id, Integer ticketId, Integer techId, String diagnosis, Double amount) {
        this(id.intValue(), ticketId.intValue(), techId.intValue(), diagnosis, amount.doubleValue());
    }


    public String toString() {
        return String.format("ID %d - %s", this.id, this.diagnosis);
    }

}
