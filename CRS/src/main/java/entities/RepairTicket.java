package entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import entities.annotations.*;
import services.xml.DateAdapter;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.time.LocalDate;

@XmlRootElement(name = "repairTicket")
@Entity
@Table(name = "repair_tickets")
public class RepairTicket {
    @JsonProperty
    @XmlElement
    @Id
    @Column(name = "ticket_id", autoIncrement = true)
    public int id;

    @JsonProperty
    @XmlElement
    @Column(name = "cust_id")
    public int custId;

    @JsonProperty
    @XmlElement
    @Column(name = "computer_desc")
    @Size(min = 1, max = 255)
    public String computerDesc;

    @JsonProperty
    @XmlElement
    @Column(name = "issue")
    @Size(min = 1, max = 255)
    public String issue;

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @XmlElement
    @XmlJavaTypeAdapter(DateAdapter.class)
    @Column(name = "date_submitted")
    public LocalDate dateSubmitted;

    @JsonProperty
    @XmlElement
    @Column(name = "status")
    @Range(min = 0, max = 6)
    public int status;

    private RepairTicket() {
    }

    public RepairTicket(int custId, String computerDesc, String issue, LocalDate dateSubmitted, int status) {
        this.custId = custId;
        this.computerDesc = computerDesc;
        this.issue = issue;
        this.dateSubmitted = dateSubmitted;
        this.status = status;
    }

    private RepairTicket(Integer custId, String computerDesc, String issue, LocalDate dateSubmitted, Integer status) {
        this(custId.intValue(), computerDesc, issue, dateSubmitted, status.intValue());
    }

    public RepairTicket(int id, int custId, String computerDesc, String issue, LocalDate dateSubmitted, int status) {
        this(custId, computerDesc, issue, dateSubmitted, status);
        this.id = id;
    }

    private RepairTicket(Integer id, Integer custId, String computerDesc, String issue, LocalDate dateSubmitted, Integer status) {
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

    public void setDateSubmitted(LocalDate dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String toString() {
        return String.format("ID %d - C%d", this.id, this.custId);
    }

}


