package entities.lists;

import com.fasterxml.jackson.annotation.JsonProperty;
import entities.Job;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "jobs")
public class JobList {
    @JsonProperty("jobs")
    private List<Job> jobList;

    @XmlElement(name = "job")
    public List<Job> getJobList() {
        return this.jobList;
    }

    public void setJobList(List<Job> jobList) {
        this.jobList = jobList;
    }
}
