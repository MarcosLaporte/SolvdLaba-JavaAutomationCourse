package entities.lists;

import com.fasterxml.jackson.annotation.JsonProperty;
import entities.JobTechnician;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "jobTechnicians")
public class JobTechnicianList {
    @JsonProperty("jobTechnicians")
    private List<JobTechnician> jobTechnicianList;

    @XmlElement(name = "jobTechnician")
    public List<JobTechnician> getJobTechnicianList() {
        return this.jobTechnicianList;
    }

    public void setJobTechnicianList(List<JobTechnician> jobTechnicianList) {
        this.jobTechnicianList = jobTechnicianList;
    }
}
