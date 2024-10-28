package entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import entities.annotations.*;
import utils.XmlLocalDateAdapter;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;

@SuppressWarnings("unused")
@XmlRootElement(name = "job")
@Table(name = "jobs")
public class Job extends Entity {
    @JsonProperty
    @XmlElement
    @Id
    @Column(name = "job_id", autoIncrement = true)
    public int id;

    @JsonProperty
    @XmlElement
    @Column(name = "ticket_id")
    public int ticketId;

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @XmlElement
    @XmlJavaTypeAdapter(XmlLocalDateAdapter.class)
    @Column(name = "date_start")
    public LocalDate dateStart;

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @XmlElement
    @XmlJavaTypeAdapter(XmlLocalDateAdapter.class)
    @Column(name = "date_finish", isNullable = true)
    public LocalDate dateFinish;

    private Job() {
    }

    public Job(int ticketId, LocalDate dateStart, LocalDate dateFinish) {
        this.ticketId = ticketId;
        this.dateStart = dateStart;
        this.dateFinish = dateFinish;
    }

    private Job(Integer ticketId, LocalDate dateStart, LocalDate dateFinish) {
        this(ticketId.intValue(), dateStart, dateFinish);
    }

    public Job(int id, int ticketId, LocalDate dateStart, LocalDate dateFinish) {
        this(ticketId, dateStart, dateFinish);
        this.id = id;
    }

    private Job(Integer id, Integer ticketId, LocalDate dateStart, LocalDate dateFinish) {
        this(id.intValue(), ticketId.intValue(), dateStart, dateFinish);
    }

    public String toString() {
        return String.format("ID %d - T%d", this.id, this.ticketId);
    }

}
