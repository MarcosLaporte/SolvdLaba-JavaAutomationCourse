package entities;

import entities.annotations.*;

@Table(name = "repair_tickets_part")
public class RepairTicketPart {
    @Id
    @Column(name = "ticket_id")
    public int ticketId;

    @Id
    @Column(name = "part_id")
    public int partId;

    @Column(name = "quantity")
    public int quantity;

    public RepairTicketPart(int ticketId, int partId, @Range(min = 1) int quantity) {
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
