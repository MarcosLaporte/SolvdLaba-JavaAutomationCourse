package entities;

import entities.annotations.*;

@Entity
@Table(name = "feedbacks")
public class Feedback {
    @Id
    @Column(name = "job_id", autoIncrement = true)
    public int jobId;

    @Column(name = "cust_comment")
    @Size(min = 1, max = 255)
    public String custComment;

    @Column(name = "rating")
    @Range(min = 1, max = 5)
    public int rating;

    @Column(name = "date_submit")
    public java.sql.Date dateSubmit;

    public Feedback(int jobId, String custComment, int rating, java.sql.Date dateSubmit) {
        this.jobId = jobId;
        this.custComment = custComment;
        this.rating = rating;
        this.dateSubmit = dateSubmit;
    }

    private Feedback(Integer jobId, String custComment, Integer rating, java.sql.Date dateSubmit) {
        this(jobId.intValue(), custComment, rating.intValue(), dateSubmit);
    }

    public String toString() {
        return String.format("ID %d - %s", this.jobId, this.custComment);
    }

}
