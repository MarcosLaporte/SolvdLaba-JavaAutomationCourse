package entities.lists;

import entities.JobTechnician;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "jobTechnicians")
public class JobTechnicianList {
    private List<JobTechnician> jobTechnicians;

    @XmlElement(name = "jobTechnician")
    public List<JobTechnician> getJobTechnicians() {
        return this.jobTechnicians;
    }

    public void setJobTechnicians(List<JobTechnician> jobTechnicians) {
        this.jobTechnicians = jobTechnicians;
    }
}
