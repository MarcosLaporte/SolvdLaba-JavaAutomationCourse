package entities;

import entities.annotations.*;
import services.xml.DateAdapter;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;

@XmlRootElement(name = "feedback")
@Entity
@Table(name = "feedbacks")
public class Feedback {
    @XmlElement
    @Id
    @Column(name = "job_id")
    public int jobId;

    @XmlElement
    @Column(name = "cust_comment")
    @Size(min = 1, max = 255)
    public String custComment;

    @XmlElement
    @Column(name = "rating")
    @Range(min = 1, max = 5)
    public int rating;

    @XmlElement
    @XmlJavaTypeAdapter(DateAdapter.class)
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

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public void setCustComment(String custComment) {
        this.custComment = custComment;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setDateSubmit(LocalDate dateSubmit) {
        this.dateSubmit = dateSubmit;
    }

    public String toString() {
        return String.format("ID %d - %s", this.jobId, this.custComment);
    }

}
