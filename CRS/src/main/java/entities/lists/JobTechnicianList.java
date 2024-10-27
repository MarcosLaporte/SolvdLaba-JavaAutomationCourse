package entities.lists;

import com.fasterxml.jackson.annotation.JsonProperty;
import entities.JobTechnician;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "jobTechnicians")
public class JobTechnicianList implements IEntityList<JobTechnician> {
    @JsonProperty("jobTechnicians")
    private List<JobTechnician> jobTechnicianList;

    private JobTechnicianList() {
    }

    private JobTechnicianList(List<JobTechnician> jobTechnicianList) {
        this.jobTechnicianList = jobTechnicianList;
    }

    @XmlElement(name = "jobTechnician")
    @Override
    public List<JobTechnician> getList() {
        return this.jobTechnicianList;
    }

    @Override
    public void setList(List<JobTechnician> jobTechnicianList) {
        this.jobTechnicianList = jobTechnicianList;
    }
}
