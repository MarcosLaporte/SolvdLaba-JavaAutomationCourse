package entities.lists;

import com.fasterxml.jackson.annotation.JsonProperty;
import entities.Job;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "jobs")
public class JobList implements IEntityList<Job> {
    @JsonProperty("jobs")
    private List<Job> jobList;

    private JobList() {
    }

    private JobList(List<Job> jobList) {
        this.jobList = jobList;
    }

    @XmlElement(name = "job")
    @Override
    public List<Job> getList() {
        return this.jobList;
    }

    @Override
    public void setList(List<Job> jobList) {
        this.jobList = jobList;
    }
}
