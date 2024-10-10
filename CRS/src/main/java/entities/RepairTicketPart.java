package entities;

public class RepairTicketPart {
    public int ticket_id;
    public int part_id;
    public int quantity;

    public RepairTicketPart(int ticket_id, int part_id, int quantity) {
        this.ticket_id = ticket_id;
        this.part_id = part_id;
        this.quantity = quantity;
    }

    private RepairTicketPart(Integer ticket_id, Integer part_id, Integer quantity) {
        this(ticket_id.intValue(), part_id.intValue(), quantity.intValue());
    }
}
