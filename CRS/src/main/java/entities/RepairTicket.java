package entities;

import entities.annotations.*;

import java.sql.Date;

@Entity
@Table(name = "repair_tickets")
public class RepairTicket {
    @Id
    @Column(name = "ticket_id", autoIncrement = true)
    public int id;

    @Column(name = "cust_id")
    public int custId;

    @Column(name = "computer_desc")
    @Size(min = 1, max = 255)
    public String computerDesc;

    @Column(name = "issue")
    @Size(min = 1, max = 255)
    public String issue;

    @Column(name = "date_submitted")
    public java.sql.Date dateSubmitted;

    @Column(name = "status")
    @Range(min = 1, max = 6)
    public int status;

    private RepairTicket() {
    }

    public RepairTicket(int custId, String computerDesc, String issue, java.sql.Date dateSubmitted, int status) {
        this.custId = custId;
        this.computerDesc = computerDesc;
        this.issue = issue;
        this.dateSubmitted = dateSubmitted;
        this.status = status;
    }

    private RepairTicket(Integer custId, String computerDesc, String issue, java.sql.Date dateSubmitted, Integer status) {
        this(custId.intValue(), computerDesc, issue, dateSubmitted, status.intValue());
    }

    public RepairTicket(int id, int custId, String computerDesc, String issue, java.sql.Date dateSubmitted, int status) {
        this(custId, computerDesc, issue, dateSubmitted, status);
        this.id = id;
    }

    private RepairTicket(Integer id, Integer custId, String computerDesc, String issue, java.sql.Date dateSubmitted, Integer status) {
        this(id.intValue(), custId.intValue(), computerDesc, issue, dateSubmitted, status.intValue());
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public void setComputerDesc(String computerDesc) {
        this.computerDesc = computerDesc;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public void setDateSubmitted(Date dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String toString() {
        return String.format("ID %d - C%d", this.id, this.custId);
    }

}


