package entities;

import entities.annotations.Column;
import entities.annotations.Entity;
import entities.annotations.Id;
import entities.annotations.Table;

@Entity
@Table(name = "jobs")
public class Job {
    @Id
    @Column(name = "job_id", autoIncrement = true)
    public int id;

    @Column(name = "ticket_id")
    public int ticket_id;

    @Column(name = "date_start")
    public java.sql.Date date_start;

    @Column(name = "date_finish", isNullable = true)
    public java.sql.Date date_finish;

    public Job(int id, int ticket_id, java.sql.Date date_start, java.sql.Date date_finish) {
        this.id = id;
        this.ticket_id = ticket_id;
        this.date_start = date_start;
        this.date_finish = date_finish;
    }

    private Job(Integer id, Integer ticket_id, java.sql.Date date_start, java.sql.Date date_finish) {
        this(id.intValue(), ticket_id.intValue(), date_start, date_finish);
    }

    public String toString() {
        return String.format("ID %d - T%d", this.id, this.ticket_id);
    }

}
