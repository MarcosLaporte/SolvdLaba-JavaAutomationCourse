package entities;


import entities.annotations.*;

@Entity
@Table(name = "jobs_technicians")
public class JobTechnician {
    @Id
    @Column(name = "job_id")
    public int jobId;

    @Id
    @Column(name = "tech_id")
    public int techId;

    @Column(name = "task")
    @Size(min = 1, max = 255)
    public String task;

    @Column(name = "done")
    public boolean done;

    public JobTechnician(int jobId, int techId, String task, boolean done) {
        this.jobId = jobId;
        this.techId = techId;
        this.task = task;
        this.done = done;
    }

    private JobTechnician(Integer jobId, Integer techId, String task, Boolean done) {
        this(jobId.intValue(), techId.intValue(), task, done.booleanValue());
    }

    public String toString() {
        return String.format("J%d - T%d", this.jobId, this.techId);
    }

}
