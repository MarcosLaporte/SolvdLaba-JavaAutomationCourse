package entities;

public class Job {
    public int job_id;
    public int ticket_id;
    public java.sql.Date date_start;
    public java.sql.Date date_finish;

    public Job(int job_id, int ticket_id, java.sql.Date date_start, java.sql.Date date_finish) {
        this.job_id = job_id;
        this.ticket_id = ticket_id;
        this.date_start = date_start;
        this.date_finish = date_finish;
    }

    private Job(Integer job_id, Integer ticket_id, java.sql.Date date_start, java.sql.Date date_finish) {
        this(job_id.intValue(), ticket_id.intValue(), date_start, date_finish);
    }
}
