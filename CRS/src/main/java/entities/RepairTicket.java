package entities;

import entities.annotations.*;

@Entity
@Table(name = "repair_tickets")
public class RepairTicket {
    @Id
    @Column(name="ticket_id", autoIncrement = true)
    public int id;

    @Column(name="cust_id")
    public int custId;

    @Column(name="computer_desc")
    public String computerDesc;

    @Column(name="issue")
    public String issue;

    @Column(name="date_submitted")
    public java.sql.Date dateSubmitted;

    @Column(name="status")
    public int status;

    public RepairTicket(int id, int custId, @Size(min = 1, max = 255) String computerDesc, @Size(min = 1, max = 255) String issue,
                        java.sql.Date dateSubmitted, @Range(min = 1, max = 6) int status) {
        this.id = id;
        this.custId = custId;
        this.computerDesc = computerDesc;
        this.issue = issue;
        this.dateSubmitted = dateSubmitted;
        this.status = status;
    }

    private RepairTicket(Integer id, Integer custId, String computerDesc, String issue, java.sql.Date dateSubmitted, Integer status) {
        this(id.intValue(), custId.intValue(), computerDesc, issue, dateSubmitted, status.intValue());
    }

    public String toString() {
        return String.format("ID %d - C%d", this.id, this.custId);
    }

}


