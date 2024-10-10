package entities;

public class JobTechnician {
    public int job_id;
    public int tech_id;
    public String task;
    public boolean done;

    public JobTechnician(int job_id, int tech_id, String task, boolean done) {
        this.job_id = job_id;
        this.tech_id = tech_id;
        this.task = task;
        this.done = done;
    }

    private JobTechnician(Integer job_id, Integer tech_id, String task, Boolean done) {
        this(job_id.intValue(), tech_id.intValue(), task, done.booleanValue());
    }
}
