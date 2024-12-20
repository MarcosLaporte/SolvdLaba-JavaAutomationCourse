package entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import entities.annotations.*;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("unused")
@XmlRootElement(name = "repairTicketPart")
@Table(name = "repair_tickets_part")
public class RepairTicketPart extends Entity {
    @JsonProperty
    @XmlElement
    @Id
    @Column(name = "ticket_id")
    public int ticketId;

    @JsonProperty
    @XmlElement
    @Id
    @Column(name = "part_id")
    public int partId;

    @JsonProperty
    @XmlElement
    @Column(name = "quantity")
    @Range(min = 1)
    public int quantity;

    private RepairTicketPart() {
    }

    public RepairTicketPart(int ticketId, int partId, int quantity) {
        this.ticketId = ticketId;
        this.partId = partId;
        this.quantity = quantity;
    }

    private RepairTicketPart(Integer ticketId, Integer partId, Integer quantity) {
        this(ticketId.intValue(), partId.intValue(), quantity.intValue());
    }

    public String toString() {
        return String.format("T%d - P%d", this.ticketId, this.partId);
    }

}
