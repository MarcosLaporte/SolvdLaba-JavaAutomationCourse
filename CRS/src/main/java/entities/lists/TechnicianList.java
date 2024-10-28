package entities.lists;

import com.fasterxml.jackson.annotation.JsonProperty;
import entities.Technician;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "technicians")
public class TechnicianList implements IEntityList<Technician> {
    @JsonProperty("technicians")
    private List<Technician> technicianList;

    private TechnicianList() {
    }

    private TechnicianList(List<Technician> technicianList) {
        this.technicianList = technicianList;
    }

    @XmlElement(name = "technician")
    @Override
    public List<Technician> getList() {
        return this.technicianList;
    }

    @Override
    public void setList(List<Technician> technicianList) {
        this.technicianList = technicianList;
    }
}
