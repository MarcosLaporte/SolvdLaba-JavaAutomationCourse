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
@XmlRootElement(name = "feedback")
@Table(name = "feedbacks")
public class Feedback extends Entity {
    @JsonProperty
    @XmlElement
    @Id
    @Column(name = "job_id")
    public int jobId;

    @JsonProperty
    @XmlElement
    @Column(name = "cust_comment")
    @Size(min = 1, max = 255)
    public String custComment;

    @JsonProperty
    @XmlElement
    @Column(name = "rating")
    @Range(min = 1, max = 5)
    public int rating;

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @XmlElement
    @XmlJavaTypeAdapter(XmlLocalDateAdapter.class)
    @Column(name = "date_submit")
    public LocalDate dateSubmit;

    private Feedback() {
    }

    public Feedback(int jobId, String custComment, int rating, LocalDate dateSubmit) {
        this.jobId = jobId;
        this.custComment = custComment;
        this.rating = rating;
        this.dateSubmit = dateSubmit;
    }

    private Feedback(Integer jobId, String custComment, Integer rating, LocalDate dateSubmit) {
        this(jobId.intValue(), custComment, rating.intValue(), dateSubmit);
    }

    public String toString() {
        return String.format("ID %d - %s", this.jobId, this.custComment);
    }

}
