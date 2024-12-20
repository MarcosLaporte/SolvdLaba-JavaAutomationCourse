package entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import entities.annotations.*;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("unused")
@XmlRootElement(name = "invoice")
@Table(name = "invoices")
public class Invoice extends Entity {
    @JsonProperty
    @XmlElement
    @Id
    @Column(name = "inv_id", autoIncrement = true)
    public int id;

    @JsonProperty
    @XmlElement
    @Column(name = "ticket_id")
    public int ticketId;

    @JsonProperty
    @XmlElement
    @Column(name = "tech_id")
    public int techId;

    @JsonProperty
    @XmlElement
    @Column(name = "diagnosis")
    @Size(min = 1, max = 255)
    public String diagnosis;

    @JsonProperty
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

    public String toString() {
        return String.format("ID %d - %s", this.id, this.diagnosis);
    }

}
