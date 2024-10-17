package entities;

import entities.annotations.Column;
import entities.annotations.Entity;
import entities.annotations.Id;
import entities.annotations.Table;

import java.sql.Date;

@Entity
@Table(name = "jobs")
public class Job {
    @Id
    @Column(name = "job_id", autoIncrement = true)
    public int id;

    @Column(name = "ticket_id")
    public int ticketId;

    @Column(name = "date_start")
    public java.sql.Date dateStart;

    @Column(name = "date_finish", isNullable = true)
    public java.sql.Date dateFinish;

    private Job() {
    }

    public Job(int ticketId, java.sql.Date dateStart, java.sql.Date dateFinish) {
        this.ticketId = ticketId;
        this.dateStart = dateStart;
        this.dateFinish = dateFinish;
    }

    private Job(Integer ticketId, java.sql.Date dateStart, java.sql.Date dateFinish) {
        this(ticketId.intValue(), dateStart, dateFinish);
    }

    public Job(int id, int ticketId, java.sql.Date dateStart, java.sql.Date dateFinish) {
        this(ticketId, dateStart, dateFinish);
        this.id = id;
    }

    private Job(Integer id, Integer ticketId, java.sql.Date dateStart, java.sql.Date dateFinish) {
        this(id.intValue(), ticketId.intValue(), dateStart, dateFinish);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public void setDateFinish(Date dateFinish) {
        this.dateFinish = dateFinish;
    }

    public String toString() {
        return String.format("ID %d - T%d", this.id, this.ticketId);
    }

}
