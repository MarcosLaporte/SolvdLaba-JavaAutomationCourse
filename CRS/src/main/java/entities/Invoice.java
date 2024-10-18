package entities;

import entities.annotations.*;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "invoice")
@Entity
@Table(name = "invoices")
public class Invoice {
    @XmlElement
    @Id
    @Column(name = "inv_id", autoIncrement = true)
    public int id;

    @XmlElement
    @Column(name = "ticket_id")
    public int ticketId;

    @XmlElement
    @Column(name = "tech_id")
    public int techId;

    @XmlElement
    @Column(name = "diagnosis")
    @Size(min = 1, max = 255)
    public String diagnosis;

    @XmlElement
    @Column(name = "amount")
    @Range(min = 1)
    public double amount;

    private Invoice() {
    }

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

    public void setId(int id) {
        this.id = id;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public void setTechId(int techId) {
        this.techId = techId;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String toString() {
        return String.format("ID %d - %s", this.id, this.diagnosis);
    }

}
