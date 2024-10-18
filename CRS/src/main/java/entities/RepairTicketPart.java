package entities;

import entities.annotations.Column;
import entities.annotations.Id;
import entities.annotations.Range;
import entities.annotations.Table;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "repairTicketPart")
@Table(name = "repair_tickets_part")
public class RepairTicketPart {
    @XmlElement
    @Id
    @Column(name = "ticket_id")
    public int ticketId;

    @XmlElement
    @Id
    @Column(name = "part_id")
    public int partId;

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

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public void setPartId(int partId) {
        this.partId = partId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String toString() {
        return String.format("T%d - P%d", this.ticketId, this.partId);
    }

}
