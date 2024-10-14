package entities;

import entities.annotations.*;

@Entity
@Table(name = "feedbacks")
public class Feedback {
    @Id
    @Column(name = "job_id", autoIncrement = true)
    public int id;

    @Column(name = "cust_comment")
    public String custComment;

    @Column(name = "rating")
    public int rating;

    @Column(name = "date_submit")
    public java.sql.Date dateSubmit;

    public Feedback(int id, @Size(min = 1, max = 255) String custComment, @Range(min = 1, max = 5) int rating, java.sql.Date dateSubmit) {
        this.id = id;
        this.custComment = custComment;
        this.rating = rating;
        this.dateSubmit = dateSubmit;
    }

    private Feedback(Integer id, String custComment, Integer rating, java.sql.Date dateSubmit) {
        this(id.intValue(), custComment, rating.intValue(), dateSubmit);
    }

    public String toString() {
        return String.format("ID %d - %s", this.id, this.custComment);
    }

}
