package entities;

public class Feedback {
    public int job_id;
    public String cust_comment;
    public int rating;
    public java.sql.Date date_submit;

    public Feedback(int job_id, String cust_comment, int rating, java.sql.Date date_submit) {
        this.job_id = job_id;
        this.cust_comment = cust_comment;
        this.rating = rating;
        this.date_submit = date_submit;
    }

    private Feedback(Integer job_id, String cust_comment, Integer rating, java.sql.Date date_submit) {
        this(job_id.intValue(), cust_comment, rating.intValue(), date_submit);
    }
}
